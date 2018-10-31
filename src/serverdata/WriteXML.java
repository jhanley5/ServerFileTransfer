package serverdata;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;

public class WriteXML {
    
   public static void overWrite (Document doc, String filepath) throws TransformerException {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);
        } catch (TransformerException te) {
            te.printStackTrace();
            throw te;
        } 
   }
    
    /* --------- UserInfo --------- */
    
   public static void addOwnedDoc (Document doc, String name, String id) {
       Element ownedDoc = doc.createElement("DOCUMENT");
       Element docID = doc.createElement("ID");
       Element docName = doc.createElement("DOCNAME");
       docName.appendChild(doc.createTextNode(name));
       docID.appendChild(doc.createTextNode(id));
       ownedDoc.appendChild(docName);
       ownedDoc.appendChild(docID);
       NodeList list = doc.getElementsByTagName("OWNED");
       Element element = (Element) list.item(0);
       element.appendChild(ownedDoc);
   }
   
   public static void addViewableDoc (Document doc, String name, String id) {
       Element ownedDoc = doc.createElement("DOCUMENT");
       Element docID = doc.createElement("ID");
       Element docName = doc.createElement("DOCNAME");
       docName.appendChild(doc.createTextNode(name));
       docID.appendChild(doc.createTextNode(id));
       ownedDoc.appendChild(docName);
       ownedDoc.appendChild(docID);
       NodeList list = doc.getElementsByTagName("VIEWABLE");
       Element element = (Element) list.item(0);
       element.appendChild(ownedDoc);
   }
   
   public static void addFriend (Document doc, String name) {
       Element friend = doc.createElement("FRIEND");
       Element friendName = doc.createElement("FRIENDNAME");
       friendName.appendChild(doc.createTextNode(name));
       friend.appendChild(friendName);
       NodeList list = doc.getElementsByTagName("FRIENDS");
       Element element = (Element) list.item(0);
       element.appendChild(friend);
   }
   
   /* --------- DocInfo --------- */
   
   public static void addDocUser (Document doc, String name) {
       Element user = doc.createElement("USER");
       Element userName = doc.createElement("USERNAME");
       userName.appendChild(doc.createTextNode(name));
       user.appendChild(userName);
       NodeList list = doc.getElementsByTagName("USERS");
       Element element = (Element) list.item(0);
       element.appendChild(user);
   }
   
   /* --------- Comments --------- */
   
   public static void addComment (Document doc, String name, String dateText, String commentText) {
       Element comment = doc.createElement("COMMENT");
       Element userName = doc.createElement("USERNAME");
       Element date = doc.createElement("DATE");
       Element text = doc.createElement("TEXT");
       userName.appendChild(doc.createTextNode(name));
       date.appendChild(doc.createTextNode(dateText));
       text.appendChild(doc.createTextNode(commentText));
       comment.appendChild(userName);
       comment.appendChild(date);
       comment.appendChild(text);
       NodeList list = doc.getElementsByTagName("COMMENTLIST");
       Element element = (Element) list.item(0);
       element.appendChild(comment);
   }
   
   /* --------- UserList --------- */
    
   public static void addUser (Document doc, String name, String password) {
       Element user = doc.createElement("USER");
       Element userName = doc.createElement("USERNAME");
       Element pass = doc.createElement("PASSWORD");
       userName.appendChild(doc.createTextNode(name));
       pass.appendChild(doc.createTextNode(password));
       user.appendChild(userName);
       user.appendChild(pass);
       NodeList list = doc.getElementsByTagName("USERLIST");
       Element element = (Element) list.item(0);
       element.appendChild(user);
   }
}


