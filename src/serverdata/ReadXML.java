package serverdata;
import org.w3c.dom.*;
import java.util.ArrayList;
public class ReadXML
{
    /* --------- UserInfo --------- */
    
    // format: "userName"
    public static String getUserName(Document doc) {
	NodeList userList = doc.getElementsByTagName("USERNAME");
	Node node = userList.item(0);
	Element element = (Element) node;
        return element.getFirstChild().getTextContent();
    }
    
    // format: arraylist "docName:docId"
    public static ArrayList<String> getOwnedDocInfo(Document doc) {
        ArrayList<String> list = new ArrayList();
        String docName;
        String docID;
        String combined;
        NodeList mainList = doc.getElementsByTagName("OWNED");
        Node mainNode = mainList.item(0);
        Element mainElement = (Element) mainNode;
        NodeList docList = mainElement.getElementsByTagName("DOCUMENT");
        for (int i = 0; docList.item(i) != null ; i++) {
            Element element = (Element) docList.item(i);
            docName = element.getElementsByTagName("DOCNAME").item(0).getFirstChild().getTextContent();
            docID = element.getElementsByTagName("ID").item(0).getFirstChild().getTextContent();
            combined = docName + ":" + docID;
            list.add(combined);
        }
        return list;
    }
    
    // format: arraylist "docName:docID"
    public static ArrayList<String> getViewableDocInfo(Document doc) {
        ArrayList<String> list = new ArrayList();
        String docName;
        String docID;
        String combined;
        NodeList mainList = doc.getElementsByTagName("VIEWABLE");
        Node mainNode = mainList.item(0);
        Element mainElement = (Element) mainNode;
        NodeList docList = mainElement.getElementsByTagName("DOCUMENT");
        for (int i = 0; docList.item(i) != null ; i++) {
            Element element = (Element) docList.item(i);
            docName = element.getElementsByTagName("DOCNAME").item(0).getFirstChild().getTextContent();
            docID = element.getElementsByTagName("ID").item(0).getFirstChild().getTextContent();
            combined = docName + ":" + docID;
            list.add(combined);
        }
        return list;
    }
    
    // format: arraylist "userName"
    public static ArrayList<String> getFriends(Document doc) {
        ArrayList<String> list = new ArrayList();
        String name;
        NodeList friendList = doc.getElementsByTagName("FRIEND");
        for (int i = 0; friendList.item(i) != null ; i++) {
            Element element = (Element) friendList.item(i);
            name = element.getElementsByTagName("FRIENDNAME").item(0).getFirstChild().getTextContent();
            list.add(name);
        }
        return list;
    }

    /* --------- DocInfo --------- */
    
    // format: "docID"
    public static String getDocID(Document doc) {
        NodeList list = doc.getElementsByTagName("DOCID");
	Node node = list.item(0);
	Element element = (Element) node;
        return element.getFirstChild().getTextContent();
    }
    
    // format: "docName"
    public static String getDocName(Document doc) {
        NodeList list = doc.getElementsByTagName("DOCNAME");
	Node node = list.item(0);
	Element element = (Element) node;
        return element.getFirstChild().getTextContent();
    }
    
    // format: "userName"
    public static String getOwnerName(Document doc) {
        NodeList list = doc.getElementsByTagName("OWNERNAME");
	Node node = list.item(0);
	Element element = (Element) node;
        return element.getFirstChild().getTextContent();
    }
    
    // format: arraylist "userName"
    public static ArrayList<String> getUsers(Document doc) {
        ArrayList<String> list = new ArrayList<>();
        String name;
        NodeList userList = doc.getElementsByTagName("USER");
        for (int i = 0; userList.item(i) != null ; i++) {
            Element element = (Element) userList.item(i);
            name = element.getElementsByTagName("USERNAME").item(0).getFirstChild().getTextContent();
            list.add(name);
        }
        return list;
    }
    
    /* --------- Comments --------- */
    
    // format: arraylist "userName:date:comment"
    public static ArrayList<String> getComments(Document doc) {
        ArrayList<String> list = new ArrayList<>();
        String userName;
        String comment;
        String date;
        String combined;
        NodeList commentList = doc.getElementsByTagName("COMMENT");
        for (int i = 0; commentList.item(i) != null ; i++) {
            Element element = (Element) commentList.item(i);
            userName = element.getElementsByTagName("USERNAME").item(0).getFirstChild().getTextContent();
            date = element.getElementsByTagName("DATE").item(0).getFirstChild().getTextContent();
            comment = element.getElementsByTagName("TEXT").item(0).getFirstChild().getTextContent();
            combined = userName + ":" + date + ": " + comment;
            list.add(combined);
        }
        return list;
    }
    
    /* --------- UserList --------- */
    
    // format: arraylist "userName:password"
    public static ArrayList<String> getAllUsers(Document doc) {
        ArrayList<String> list = new ArrayList<>();
        String userName;
        String password;
        String combined;
        NodeList commentList = doc.getElementsByTagName("USER");
        for (int i = 0; commentList.item(i) != null ; i++) {
            Element element = (Element) commentList.item(i);
            userName = element.getElementsByTagName("USERNAME").item(0).getFirstChild().getTextContent();
            password = element.getElementsByTagName("PASSWORD").item(0).getFirstChild().getTextContent();
            combined = userName + ":" + password;
            list.add(combined);
        }
        return list;
    }
}
