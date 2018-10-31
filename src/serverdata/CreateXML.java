package serverdata;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;

public class CreateXML {
    public static void createComments(String filepath, String id) throws TransformerException, ParserConfigurationException {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            
            
            Element rootElement = doc.createElement("COMMENTS");
            Element comments = doc.createElement("COMMENTLIST");
            Element docID = doc.createElement("DOCID");
           
            docID.appendChild(doc.createTextNode(id));
            
            rootElement.appendChild(docID);
            rootElement.appendChild(comments);
            
            doc.appendChild(rootElement);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);
        } catch (TransformerException te) {
            te.printStackTrace();
            throw te; 
        } catch (ParserConfigurationException pe) {
            pe.printStackTrace();
            throw pe; 
        }     
    }
    
    public static void createUserInfo(String filepath, String name) throws TransformerException, ParserConfigurationException  {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            
            Element rootElement = doc.createElement("USER");
            Element userName = doc.createElement("USERNAME");
            userName.appendChild(doc.createTextNode(name));
            Element friends = doc.createElement("FRIENDS");
            Element owned = doc.createElement("OWNED");
            Element viewable = doc.createElement("VIEWABLE");
            rootElement.appendChild(userName);
            rootElement.appendChild(friends);
            rootElement.appendChild(owned);
            rootElement.appendChild(viewable);
            doc.appendChild(rootElement);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);
        } catch (TransformerException te) {
            te.printStackTrace();
            throw te; 
        } catch (ParserConfigurationException pe) {
            pe.printStackTrace();
            throw pe; 
        }   
    }
    
    public static void createDocInfo(String filepath, String id, String docname, String ownername) throws TransformerException, ParserConfigurationException  {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            
            Element rootElement = doc.createElement("DOCINFO");
            Element docName = doc.createElement("DOCNAME");
            docName.appendChild(doc.createTextNode(docname));
            Element docID = doc.createElement("DOCID");
            docID.appendChild(doc.createTextNode(id));
            Element ownerName = doc.createElement("OWNERNAME");
            ownerName.appendChild(doc.createTextNode(ownername));
            Element users = doc.createElement("USERS");
            rootElement.appendChild(docName);
            rootElement.appendChild(docID);
            rootElement.appendChild(ownerName);
            rootElement.appendChild(users);
            doc.appendChild(rootElement);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);
        } catch (TransformerException te) {
            te.printStackTrace();
            throw te; 
        } catch (ParserConfigurationException pe) {
            pe.printStackTrace();
            throw pe; 
        }   
    }
}
