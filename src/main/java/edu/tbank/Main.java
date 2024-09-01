package edu.tbank;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tbank.dto.City;
import edu.tbank.parser.XmlParser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        File file = new File("city.json");

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            City city = objectMapper.readValue(file, City.class);

            XmlParser.convertStringToXMLFile(city);
            logger.info("XML file (response.xml) successfully created");
        } catch (IOException | ParserConfigurationException | TransformerException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }


}