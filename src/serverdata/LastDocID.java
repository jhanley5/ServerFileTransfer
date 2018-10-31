package serverdata;
import org.w3c.dom.*;

public class LastDocID {
    
    /* Returns LastID as an integer value */
    public static int getLast(Document doc) {
            NodeList userList = doc.getElementsByTagName("ID");
            Node node = userList.item(0);
            Element element = (Element) node;
            return  Integer.parseInt(element.getFirstChild().getTextContent());

    /* Sets LastID to given integer value */
    } 
    public static void setLast(Document doc, int id) throws Exception {
            NodeList userList = doc.getElementsByTagName("ID");
            Node node = userList.item(0);
            node.setTextContent(Integer.toString(id));
    }
}
