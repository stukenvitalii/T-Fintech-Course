package edu.tbank.parser;

import edu.tbank.dto.City;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XmlParser {
    private XmlParser() {}
    private static final Logger logger = Logger.getLogger(XmlParser.class.getName());

    public static void convertStringToXMLFile(City city) throws ParserConfigurationException, TransformerException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.newDocument();

        Element coords = document.createElement("coords");
        Element cityEl = document.createElement("city");

        document.appendChild(cityEl);
        cityEl.setAttribute("slug", city.getSlug());
        cityEl.appendChild(coords);

        coords.setAttribute("lon", String.valueOf(city.getCoords().getLon()));
        coords.setAttribute("lat", String.valueOf(city.getCoords().getLat()));

        Transformer trans = TransformerFactory.newInstance().newTransformer();

        try (FileWriter fileWriter = new FileWriter("response.xml")) {
            trans.transform(new DOMSource(document), new StreamResult(fileWriter));
            logger.log(Level.INFO, "XML file written to {0}", fileWriter);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
