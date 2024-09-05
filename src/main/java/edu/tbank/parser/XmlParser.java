package edu.tbank.parser;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import edu.tbank.dto.City;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
@UtilityClass
public class XmlParser {
    private final static XmlMapper xmlMapper = new XmlMapper();
    public static void convertStringToXMLFile(City city) {
        try {
            xmlMapper.writeValue(new File("response.xml"), city);
            log.info("XML file written to response.xml");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
