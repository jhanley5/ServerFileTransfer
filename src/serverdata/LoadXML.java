package serverdata;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class LoadXML
{
    public static Document LoadXML(String filePath) throws SAXException, IOException, ParserConfigurationException
    {
        try {
        File file = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        return doc;
    } catch (IOException ie) {
        ie.printStackTrace();
        throw ie;
    } catch (SAXException se) {
        se.printStackTrace();
        throw se;
    } catch (ParserConfigurationException pe) {
        pe.printStackTrace();
        throw pe;
    }
}
}

 