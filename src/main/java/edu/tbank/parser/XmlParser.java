package edu.tbank.parser;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import edu.tbank.dto.City;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XmlParser {
    private XmlParser() {}
    private static final Logger logger = Logger.getLogger(XmlParser.class.getName());

    public static void convertStringToXMLFile(City city) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            xmlMapper.writeValue(new File("response.xml"), city);
            logger.log(Level.INFO, "XML file written to response.xml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
