package edu.tbank.hw2;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tbank.hw2.dto.City;
import edu.tbank.hw2.parser.XmlParser;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class Main {

    public static void main(String[] args) {
        try {
            File file = new File("src/main/resources/city.json");
            log.debug("File successfully opened");

            ObjectMapper objectMapper = new ObjectMapper();
            City city = objectMapper.readValue(file, City.class);
            log.debug("Object successfully read from file");

            XmlParser.convertStringToXMLFile(city);
            log.info("XML file (response.xml) successfully created");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}