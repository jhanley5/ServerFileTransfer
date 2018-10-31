package serverdata;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;

/*
* Class that contains functions for accessing information via XML
*/
public class ServerData 
{
    Document doc;   // Creates Document Object
    
    /*
    * Function that verifies if username and password entered are either valid for login or not yet used so as to create a new account
    * Parameters are two string variables: Username and Password entered
    * Returns Boolean signifying validity
    */         
    public Boolean getUsers(String usr, String pass)
    {
        ArrayList<String> data;   
        boolean valid = false;
        try {
            doc = (LoadXML.LoadXML("XMLFiles\\UserList.XML"));
        } catch (SAXException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       data = ReadXML.getAllUsers(doc);
       
       for(String s : data)
       {
           String[] tmp;
           tmp = s.split(":");
           
           if(tmp[0].equals(usr) && tmp[1].equals(pass))
           {
               valid = true;
               break;
           }
       }
       
       return valid; 
    }
    
    /*
    * Function that adds a new User
    * Parameters are two string variables: Username and Password entered
    */  
    public void addUser(String usr, String pass)
    {
   
        try {
            doc = (LoadXML.LoadXML("XMLFiles\\UserList.XML"));
        } catch (SAXException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WriteXML.addUser(doc, usr, pass);
        
        try {
            WriteXML.overWrite(doc, "XMLFiles\\UserList.XML");
        } catch (TransformerException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String filepath = "XMLFiles\\" + usr +"_Info.xml";
        
        try {
            CreateXML.createUserInfo(filepath, usr);
        } catch (TransformerException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    * Function that gathers all initial information on a user
    * Parameter is a string variable: username of client
    * Returns String variable with all required information
    */  
    public String Populate(String usr)
    {
        String combined = "";
        ArrayList<String> Friends;
        ArrayList<String> Owned;
        ArrayList<String> Viewable;
   
        try {
            doc = (LoadXML.LoadXML("XMLFiles\\"+usr+"_Info.XML"));
        } catch (SAXException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Friends = ReadXML.getFriends(doc);
        for(String tmp : Friends)
        {
            combined = combined+tmp+"`";
        }
        combined += "&";
        
        Owned = ReadXML.getOwnedDocInfo(doc);
        for(String tmp : Owned)
        {
            combined = combined+tmp+"`";
        }
        combined += "&";
        
        Viewable = ReadXML.getViewableDocInfo(doc);
        for(String tmp : Viewable)
        {
            combined = combined+tmp+"`";
        }
        combined += "&";
                
        return combined;
    }
    
    /*
    * Function that acquires all files a user is eligible to view from their friends
    * Parameter is a string variable: username of client
    * Returns String with name of all friends' files client is allowed to view
    */  
    public String FriendFiles(String usr)
    {
        String combined = "";
        ArrayList<String> Viewable;
   
        try {
            doc = (LoadXML.LoadXML("XMLFiles\\"+usr+"_Info.XML"));
        } catch (SAXException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
      
        Viewable = ReadXML.getViewableDocInfo(doc);
        for(String tmp : Viewable)
        {
            combined = combined+tmp+"`";
        }
        combined += "&";
                
        return combined;
    }
    
    /*
    * Function that acquires list of users eligible to view a specific file
    * Parameters are two string variables: username of client and documentID
    * Returns string with list of all users already able to view file, and name of fileowner
    */  
    public String Allowed(String Usrnme, String docID)
    {
        ArrayList<String> Usrs = new ArrayList<String>();
        String combined = "";
         try {
            doc = (LoadXML.LoadXML("XMLFiles\\"+docID+"_Info.XML"));
        } catch (SAXException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String owner = ReadXML.getOwnerName(doc);
        if(owner.equals(Usrnme))
        {
            
            Usrs = ReadXML.getUsers(doc);
            
            for(String tmp : Usrs)
            {
                combined = combined+tmp+":";
            }
            combined += "`"+owner;
        }
        else 
        {
            combined = "None`"+owner;
        }
        
        
        return combined;
    }
    
    /*
    * Function that gets all comments about a specific file
    * Parameter is one string variable: documentID
    * Returns String with all comments 
    */  
    public String comments(String docID)
    {
        ArrayList<String> cmnts = new ArrayList<String>();
        String combined = "";
         try {
            doc = (LoadXML.LoadXML("XMLFiles\\"+docID+"_Comments.XML"));
        } catch (SAXException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        cmnts = ReadXML.getComments(doc);
        
        for(String tmp : cmnts)
        {
            combined = combined+tmp+"`";
        }
        
        return combined;
    }
    
    /*
    * Function that adds a new friend
    * Parameters are two string variables: username of client and username of friend to be added
    * Returns Boolean of whether or not friend was added
    */  
    public Boolean addFriend(String usr, String newfriend)
    {
        ArrayList<String> users;   
        boolean real = false;
        boolean added = false;
        
        
        try {
            doc = (LoadXML.LoadXML("XMLFiles\\UserList.XML"));
        } catch (SAXException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        users = ReadXML.getUsers(doc);
        
        for(String n:users)
        {
            if(n.equals(newfriend))
            {
                real = true;
                break;
            }
        }
        
        users.clear();
        
        if(real == true)
        {
            try {
                doc = (LoadXML.LoadXML("XMLFiles\\"+usr+"_Info.XML"));
            } catch (SAXException ex) {
                Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            users = ReadXML.getFriends(doc);
            
            for(String n:users)
            {
                if(n.equals(newfriend) )
                {
                    real = false;
                    break;
                }
            }
            
            if(real != false)
            {
                WriteXML.addFriend(doc, newfriend);

                try {
                    WriteXML.overWrite(doc, "XMLFiles\\"+usr+"_Info.XML");
                } catch (TransformerException ex) {
                    Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
                }
                added = true;
            }
        }
        
       return added; 
    }
    
    /*
    * Function that adds a viewer able to see a file
    * Parameters are three string variables: username of viewer to be added, document name, and documentID
    */  
    public void addViewer(String viewer, String docName, String docID)
    {
        try {
            doc = (LoadXML.LoadXML("XMLFiles\\"+docID+"_Info.XML"));
        } catch (SAXException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WriteXML.addDocUser(doc, viewer);
        
        try {
            WriteXML.overWrite(doc, "XMLFiles\\"+docID+"_Info.XML");
        } catch (TransformerException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            doc = (LoadXML.LoadXML("XMLFiles\\"+viewer+"_Info.XML"));
        } catch (SAXException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WriteXML.addViewableDoc(doc, docName, docID);
        
        try {
            WriteXML.overWrite(doc, "XMLFiles\\"+viewer+"_Info.XML");
        } catch (TransformerException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    * Function that adds a comment to comment file
    * Parameters are four string variables: documentID, username of commentor, date commented on, and comment
    */  
    public void addComment(String docID, String usr, String date, String cmnt)
    {
        try {
            doc = (LoadXML.LoadXML("XMLFiles\\"+docID+"_Comments.XML"));
        } catch (SAXException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WriteXML.addComment(doc, usr, date, cmnt);
        
        try {
            WriteXML.overWrite(doc, "XMLFiles\\"+docID+"_Comments.XML");
        } catch (TransformerException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    * Function that updates last document ID
    * Returns a string with value of most recent document ID
    */  
    public String LastdocID()
    {
         try {
            doc = (LoadXML.LoadXML("XMLFiles\\LastID.XML"));
        } catch (SAXException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        int lastid = LastDocID.getLast(doc);
        lastid++;
        try { 
            LastDocID.setLast(doc,lastid);
        } catch (Exception ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            WriteXML.overWrite(doc, "XMLFiles\\LastID.XML");
        } catch (TransformerException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
        String ID = Integer.toString(lastid);
        
        return ID;
        
    }
    
    /*
    * Function that creates necessary files after a file has been uploaded
    * Parameters are four string variables: file path, documentID, document name, and username of client
    */  
    public void createFileInfo(String path,String id, String docName, String usr)
    {
        try {
            CreateXML.createDocInfo(path, id, docName, usr);
        } catch (TransformerException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String path2 = "XMLFiles\\"+id+"_Comments.xml";
        try {
            CreateXML.createComments(path2, id);
        } catch (TransformerException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         try {
            doc = (LoadXML.LoadXML("XMLFiles\\"+usr+"_Info.XML"));
        } catch (SAXException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        WriteXML.addOwnedDoc(doc, docName, id);
        try {
            WriteXML.overWrite(doc, "XMLFiles\\"+usr+"_Info.XML");
        } catch (TransformerException ex) {
            Logger.getLogger(ServerData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
