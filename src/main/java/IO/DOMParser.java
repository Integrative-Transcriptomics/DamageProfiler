package IO;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by neukamm on 06.10.2016.
 *
 *
 * This class parses XML file from NCBI te get scientific name of a specie.
 *
 */
public class DOMParser {

    public String parse(String filepathXML) throws ParserConfigurationException, SAXException, IOException {

        String species = null;
        try {

            File inputFile = new File(filepathXML);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Item");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (eElement.getAttribute("Name").equals("ScientificName")) {
                        species = eElement.getTextContent();
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return species;
    }

}
