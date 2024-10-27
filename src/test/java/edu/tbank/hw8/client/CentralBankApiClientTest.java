package edu.tbank.hw8.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.tbank.hw8.entity.Valute;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CentralBankApiClientTest {

    private WireMockServer wireMockServer;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @SpyBean
    private CentralBankApiClient centralBankApiClient;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(new WireMockConfiguration().port(7070));
        wireMockServer.start();
        WireMock.configureFor("localhost", 7070);
        clearCache();
        resetCircuitBreaker();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    void clearCache() {
        Objects.requireNonNull(cacheManager.getCache("dailyRates")).clear();
    }

    void resetCircuitBreaker() {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("bankClient");
        circuitBreaker.reset();
    }

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("currency-app.bank-api-url", () -> "http://localhost:7070");
    }

    @Test
    @DisplayName("Get Daily Rates: Valid Response Returns Valutes")
    void getDailyRates_ValidResponse_ReturnsValutes() {

        wireMockServer.stubFor(get(urlEqualTo("/XML_daily.asp/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE,
                                MediaType.APPLICATION_XML_VALUE)
                        .withBody(""" 
                                <ValCurs Date="05.10.2024" name="Foreign Currency Market">
                                    <Valute ID="R01820">
                                        <NumCode>392</NumCode>
                                        <CharCode>JPY</CharCode>
                                        <Nominal>100</Nominal>
                                        <Name>Японских иен</Name>
                                        <Value>64,6782</Value>
                                        <VunitRate>0,646782</VunitRate>
                                    </Valute>
                                    <Valute ID="R01020A">
                                        <NumCode>944</NumCode>
                                        <CharCode>AZN</CharCode>
                                        <Nominal>1</Nominal>
                                        <Name>Азербайджанский манат</Name>
                                        <Value>55,8059</Value>
                                        <VunitRate>55,8059</VunitRate>
                                    </Valute>
                                </ValCurs>""")));

        Mockito.reset(centralBankApiClient);

        List<Valute> valutes = centralBankApiClient.getDailyRates();

        assertEquals(2, valutes.size());
        assertEquals("JPY", valutes.get(0).getCharCode());
        assertEquals("AZN", valutes.get(1).getCharCode());
    }

    @Test
    @DisplayName("Get Daily Rates: Null Response Triggers Fallback Method")
    void getDailyRates_NullResponse_FallbackMethodCalled() {
        wireMockServer.stubFor(get(urlEqualTo("/XML_daily.asp/"))
                .willReturn(aResponse()
                        .withStatus(503)
                ));

        List<Valute> valutes = centralBankApiClient.getDailyRates();

        verify(centralBankApiClient).fallbackDailyRates(ArgumentMatchers.any(HttpServerErrorException.class));
    }

    @Test
    @DisplayName("Get Daily Rates: Null Response Returns Empty List")
    void getDailyRates_NullResponse_EmptyListReturned() {
        wireMockServer.stubFor(get(urlEqualTo("/XML_daily.asp/"))
                .willReturn(aResponse()
                        .withStatus(503)
                ));

        List<Valute> valutes = centralBankApiClient.getDailyRates();

        assertEquals(0, valutes.size());
    }

    @Test
    @DisplayName("Circuit Breaker: Closed State with All Calls Succeed")
    void circuitBreaker_ClosedState_AllCallsSucceed() throws InterruptedException {
        // Simulate failure response to open the circuit breaker
        wireMockServer.stubFor(get(urlEqualTo("/XML_daily.asp/"))
                .willReturn(aResponse()
                        .withStatus(503)
                ));

        for (int i = 0; i < 3; i++) {
            try {
                centralBankApiClient.getDailyRates();
            } catch (Exception ignored) {
            }
            clearCache();
        }

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("bankClient");
        assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());

        Thread.sleep(11000);
        assertEquals(CircuitBreaker.State.HALF_OPEN, circuitBreaker.getState());

        // Simulate successful response
        wireMockServer.stubFor(get(urlEqualTo("/XML_daily.asp/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                        .withBody(""" 
                                <ValCurs Date="05.10.2024" name="Foreign Currency Market">
                                    <Valute ID="R01820">
                                        <NumCode>392</NumCode>
                                        <CharCode>JPY</CharCode>
                                        <Nominal>100</Nominal>
                                        <Name>Японских иен</Name>
                                        <Value>64,6782</Value>
                                    </Valute>
                                </ValCurs>""")));

        for (int i = 0; i < 3; i++) {
            centralBankApiClient.getDailyRates();
            clearCache();
        }

        assertEquals(CircuitBreaker.State.CLOSED, circuitBreaker.getState());
    }

    @Test
    @DisplayName("Circuit Breaker: Open State Triggered After Failures")
    void circuitBreaker_OpenState_TriggeredAfterFailures() {
        wireMockServer.stubFor(get(urlEqualTo("/XML_daily.asp/"))
                .willReturn(aResponse()
                        .withStatus(503)
                ));

        for (int i = 0; i < 3; i++) {
            centralBankApiClient.getDailyRates();
            clearCache();
        }

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("bankClient");
        assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());

        List<Valute> valutes = centralBankApiClient.getDailyRates();
        assertTrue(valutes.isEmpty());
    }

    @Test
    @DisplayName("Circuit Breaker: Half Open State Allows Limited Calls")
    void circuitBreaker_HalfOpenState_AllowsLimitedCalls() throws InterruptedException {
        wireMockServer.stubFor(get(urlEqualTo("/XML_daily.asp/"))
                .willReturn(aResponse()
                        .withStatus(503)
                ));

        for (int i = 0; i < 3; i++) {
            centralBankApiClient.getDailyRates();
            clearCache();
        }

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("bankClient");
        assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());

        Thread.sleep(11000);
        assertEquals(CircuitBreaker.State.HALF_OPEN, circuitBreaker.getState());

        wireMockServer.stubFor(get(urlEqualTo("/XML_daily.asp/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                        .withBody(""" 
                                <ValCurs Date="05.10.2024" name="Foreign Currency Market">
                                    <Valute ID="R01820">
                                        <NumCode>392</NumCode>
                                        <CharCode>JPY</CharCode>
                                        <Nominal>100</Nominal>
                                        <Name>Японских иен</Name>
                                        <Value>64,6782</Value>
                                        <VunitRate>0,646782</VunitRate>
                                    </Valute>
                                </ValCurs>""")));

        for (int i = 0; i < 3; i++) {
            List<Valute> valutes = centralBankApiClient.getDailyRates();
            assertEquals(1, valutes.size());
            clearCache();
        }

        assertEquals(CircuitBreaker.State.CLOSED, circuitBreaker.getState());
    }
}