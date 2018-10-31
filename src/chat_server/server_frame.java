package chat_server;
import serverdata.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date; 
import javax.swing.*;

/**
 * Class that displays an interface for server 
 * Able to start server
 * Able to stop server 
 * Able to view users online
 * Able to clear text area
 * Able to enter port number
 */
public class server_frame extends javax.swing.JFrame 
{
   ArrayList clientOutputStreams;   // ArrayList of Client output stream
   ArrayList<String> users; // ArrayList of users
   ServerSocket serverSock; // Socket of server 
   int port;    // Port number
   boolean open = false;    // Boolean variable associated with whether server is open or not

   public class ClientHandler implements Runnable	
   {
       ServerData SD = new ServerData();    // Creates new ServerData object to use XML to gather information
       BufferedReader reader;   // BufferedReader object to read input from socket connection input stream
       Socket sock; // Socket object 
       PrintWriter client;  // PrintWriter object to write output to socket output stream
       
       public ClientHandler(Socket clientSocket, PrintWriter user)  // Constructor that creates a handler for clients passed parameters of socket and printwriter objects
       {
            client = user;  // Set PrintWriter object equal to passed in PrintWriter object 
            try 
            {
                sock = clientSocket;    // Sets socket object equal to passed in Socket object
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());  // Creates new InputStreamReader object set to read input from socket input stream
                reader = new BufferedReader(isReader);  // Creates new BufferedReader object set to buffer input read from InputStreamReader object
            }
            catch (IOException ex) 
            {
                ta_chat.append("Unexpected error... \n");   // Appends error to chat textarea
            }
       }

       @Override
       public void run()    // Overrides run method so thread performs functions wanted
       {
            String message;  // Declares string variable that stores input read from socket connection input stream
            String[] data;  // String array stores message parsed by a token
            String usr = "";    // String variable that stores clients name
            Boolean match = false;  // Boolean variable that represents if user information for login exists
            Boolean online = false; // Determines if user information entered for login is already being used in program
    
            try 
            {
                while ((message = reader.readLine()) != null && open == true)   // While message from input stream is not null and server is open  
                {
                    data = message.split("&");
                    if(data.length > 2)
                    {
                        if(data[2].equals("Verify"))
                        {
                            ta_chat.append("\nReceived: " + data[0] + ":"+ data[2] +"\n");    // Append recieved message  
                        }
                        else
                        {
                            ta_chat.append("\nReceived: " + message + "\n");    // Append recieved message 
                        }
                    }
                    else
                    {
                        ta_chat.append("\nReceived: " + message + "\n");    // Append recieved message 
                    }

                    if(data[1].equals("Ready")) // If element equals Ready
                    {
                        usr = data[0];  // Initializes username string variable with string element 
                        
                        String pop = SD.Populate(data[0]);  // Initializes string variable with string value returned from calling function Populate of ServerData class passing in username
                        client.println(pop+"Populate"); // PrintWriter object writes to client string variable and command Populate
                        client.flush(); // Flushes PrintWriter Object's buffer
                    }
                    else if(data[1].equals("FriendsFiles")) // If element equals FriendsFiles
                    {
                        String lst = SD.FriendFiles(data[0]);   // Initializes string variable with string value returned from calling function FriendFiles of ServerData class passing in username
                        client.println(lst+"FrFile");   // PrintWriter object writes to client string variable and command FrFile
                        client.flush(); // Flushes PrintWriter Object's buffer
                    }
                    else if(data[2].equals("Friend"))   // If element equals Friend 
                    {
                        boolean added = SD.addFriend(data[0],data[1]);  // Initializes boolean variable with boolean value returned from calling function addFriend of ServerData class passing in username of client and of friend to add
                        if(added == true)   // If friend added
                        {
                            client.println(data[1]+"&Added");   // PrintWriter object writes to client added friend's username and command Added
                            client.flush();  // Flushes PrintWriter Object's buffer
                        }
                        else
                        {
                            client.println(data[1]+"&NotAdded");    // PrintWriter object writes to client added friend's username and command NotAdded
                            client.flush(); // Flushes PrintWriter Object's buffer
                        }
                    }
                    else if(data[2].equals("Display"))  // If element equals Display
                    {
                        String Allowed = SD.Allowed(data[0], data[1]);  // Initializes string variable with string value returned from calling function Allowed of ServerData class passing in username of client and fileID 
                        
                        try
                        {  
                            String file = "Documents\\" + data[1] + ".txt";  // Initializes string variable with path to file 
                            File newFile = new File(file);  // Creates new File object set to path of file
                            int size = (int)newFile.length();   // Initializes integer variable with size of file
                            client.println(Allowed+"&"+size+"&Display");    // PrintWriter object writes to client string variable , size of file, and command to Display
                            client.flush(); // Flushes PrintWriter Object's buffer
                            byte[] bytearray = new byte[size];  // Creates new byte array of file's size
                            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(newFile));    // Creates new BufferedInputSream object set to read from file
                            bis.read(bytearray, 0, bytearray.length);   // Reads file into byte array
                            OutputStream os = sock.getOutputStream();   // Creates new OutputStream object that is set to write to socket output stream
                            os.write(bytearray, 0, bytearray.length);   // Writes contents of byte array to client
                            os.flush(); // Flushes OutputStream object buffer
                            bis.close();    // Closes InputSream object reading file
                        }
                        catch(IOException ex)
                        {
                            client.println("Error&Failed to write file for display&Chat");  // PrintWriter object writes to client error 
                            client.flush(); // Flushes PrintWriter object buffer 
                        } 
                    }
                    else if(data[2].equals("Connected"))    // If element equals Connected
                    {
                        tellEveryone((data[0] + "&has connected&Chat"));    // Calls function that broadcasts message to all clients
                        users.add(data[0]);     // Adds username to list of users online
                    }
                    else if (data[2].equals("Disconnect"))  // If element equals Disconnect
                    {
                        tellEveryone((data[0] + "&has disconnected&Chat")); // Calls function that broadcasts message to all clients
                        users.remove(data[0]);  // Removes username from list of users online
                        sock.close();   // Closes socket connection 
                        break;  // Breaks out of while loop
                        
                    } 
                    else if (data[2].equals("Chat"))    // If element equals Chat
                    {
                        tellEveryone(message);  // Calls function that broadcasts message to all clients
                    } 
                    else if(data[2].equals("PrevComments")) // If element equals PrevComments
                    {
                        String comments = SD.comments(data[0]); // Initializes string variable with string value returned from calling comments function of ServerData class passing in fileID 
                        client.println(comments+"&"+"DisplayComments"); // PrintWriter object writes to client string variable and command DisplayComments
                        client.flush(); // Flushes PrintWriter object's buffer
                    }
                    else if(data[3].equals("Upload"))   // If element equals Upload
                    {
                        String docID = SD.LastdocID();  // Initializes string variable with string value returned from calling LastdocID function of ServerData class 
                        String filename = data[0];
                        int size = Integer.parseInt(data[1]);   // Initializes integer variable with string converted to integer
                        String file = "Documents\\" + docID + ".txt";   // Initializes string variable with a file path
                        
                        byte[] mybytearray = new byte[size];    // Creates new byte array object set to file size
                        InputStream is = sock.getInputStream(); // Creates new InputStream object set to read input from socket input stream
                        FileOutputStream fos = new FileOutputStream(file);  // Creates new FileOutputStream object to write to file  
                        BufferedOutputStream bos = new BufferedOutputStream(fos);   // Creates new BufferedOutputStream object that buffers FileOutputStream object 
                        int bytesRead = is.read(mybytearray, 0, mybytearray.length);   // Reads input from input stream storing it into byte array and Iniializes integer variable with number of bytes read  
                        bos.write(mybytearray, 0, bytesRead);   // BufferedOutputStream object writes to file contents of byte array
                        bos.close();    // Closes BufferedOutputStream object writing to file
                        
                        String path = "XMLFiles\\" + docID + "_Info.xml";   // Initializes string variable with a file path
                        SD.createFileInfo(path,docID,filename,data[2]); // Calls createFileInfo function of ServerData class passing in file path, fileID, file name, and username
                        
                        String myfiles = filename+":"+docID;    // Initializes string variable with filename and fileID
                        client.println(myfiles+"&Uploaded");    // PrintWriter object writes to client string variable and command Uploaded
                        client.flush(); // Flushes PrintWriter object's buffer
                        
                    }
                    else if (data[3].equals("Verify"))  // If element equals Verify
                    {
                        PrintWriter isWriter = new PrintWriter(sock.getOutputStream()); // Creates new PrintWriter object that is set to write to socket output stream
                        usr = data[0];  // Initializes string variable initialized with username
                        String pass = data[1];  // Initializes string variable with password
                        boolean valid = SD.getUsers(usr,pass);  // Initializes boolean variable with boolean returned from calling getUsers function of ServerData class passing in username and password
                        
                        if(valid == true)   // If username and password entered are valid
                        {
                            for(String tmp : users) // For number of elements in ArrayList users
                            {
                                if(tmp.equals(usr)) // If string tmp equals username
                                {
                                    online = true;  // User is already online so not available
                                    break;  // Break out of for loop
                                }
                            }
                            
                            if(online == false) // If user associated with username and password is not online
                            {
                                isWriter.println("Username&Password&Valid");    // PrintWriter object writes message to client
                                ta_chat.append("Sending:Valid");    // Appends message to server textarea
                                users.add(data[0]); //Adds username to list of users online
                                clientOutputStreams.remove(client); // Removes PrintWriter object from list of output streams 
                                match = true;   // Means username and password entered matched an account
                            }
                            else
                            {
                                isWriter.println("Username&Password&Unavailable");  // PrintWriter object writes message to client
                                ta_chat.append("Sending:Unavailable");  // Appends message to server textarea
                                isWriter.flush();   // Flushes PrintWriter object's buffer
                                ta_chat.setCaretPosition(ta_chat.getDocument().getLength());    // Sets caret position to the end of textarea
                            }
                            
                        }
                        else
                        {
                            isWriter.println("Username&Password&Invalid");  // PrintWriter object writes message to client
                            ta_chat.append("Sending:Invalid");  // Appends message to server textarea
                        }
                        isWriter.flush();   // Flushes PrintWriter object's buffer
                        ta_chat.setCaretPosition(ta_chat.getDocument().getLength());    // Sets caret position to the end of textarea
                        
                        
                        
                    } 
                    else if (data[3].equals("Create")) // If element equals Create
                    {
                        PrintWriter isWriter = new PrintWriter(sock.getOutputStream()); // Creates new PrintWriter object that is set to write to socket output stream
                        usr = data[0];  // Initializes string variable with username entered
                        String pass = data[1];  // Initializes string variable with password entered
                        boolean taken = SD.getUsers(usr, pass); // Initializes boolean variable with boolean returned from calling getUsers function of ServerData class passing in username and password
                        
                        if(taken == false)  // If username and password do not already exist
                        {
                            SD.addUser(usr, pass);  // Calls addUser function of ServerData class to add username and password to list of users
                            isWriter.println("Username&Password&Added");    // PrintWriter object writes message to client   
                            ta_chat.append("Sending:Added");    // Appends message to server textarea
                            clientOutputStreams.remove(client);     // Removes PrintWriter object from list of output streams 
                        }
                        else
                        {                                
                            isWriter.println("Username&Unavailable&Taken"); // PrintWriter object writes message to client
                            ta_chat.append("Sending:Taken");    // Appends message to server textarea
                        }  
                        isWriter.flush();   // Flushes PrintWriter object's buffer
                        ta_chat.setCaretPosition(ta_chat.getDocument().getLength());    // Sets caret position to the end of textarea
                    } 
                    else if(data[3].equals("Comment"))  // If element equals Comment
                    {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");   // Creates DateFormat object
                        Date date = new Date(); // Creates new Date object
                        String stamp = dateFormat.format(date); // Initializes string variable with Date object fomatted by DateFormat object
                        SD.addComment(data[0], data[1], stamp, data[2]);    // Calls ServerData addComment function passing in fileID, username, date, and comment made
                        client.println(data[1]+":"+stamp+": "+ data[2]+"&Commented");   // PrintWriter object writes to client username,date,comment made, and command Commented
                        client.flush(); // Flushes PrintWriter Object's buffer 
                        ta_chat.append("Sending:Commented");    // Appends message to textarea
                        ta_chat.setCaretPosition(ta_chat.getDocument().getLength());    // Sets caret to end of textarea
                    }
                    else if(data[3].equals("Viewer"))   // If element equals Viewer
                    {
                        SD.addViewer(data[0],data[1],data[2]);  // Calls ServerData class function addViewer passing in viewer name, file name, and fileiD
                        client.println(data[0]+"&AddedViewer"); // PrintWriter object writes to name of viewer added, and command AddedViewer
                        client.flush(); // Flushes PrintWriter object's buffer
                        
                    }
                    else 
                    {
                        ta_chat.append("No Conditions were met. \n");   // Appends message to textarea
                    }
                } 
            } 
            catch (IOException  ex) 
            {
               clientOutputStreams.remove(client);  // Removes PrintWriter object from list of output streams
               users.remove(usr);   // Removes username from list of users online
               tellEveryone(usr+"&Lost Connection&Chat");   // Calls function that broadcasts message to everyone
               ta_chat.append("Lost a connection. \n"); // Appends message to textarea               
            } 
            
            if(match == true)   // If username and password logged in successfully
            {
                tellEveryone((usr + "&has connected&Chat"));    // Calls function that broadcasts message to everyone
            }
	} 
    }

    /**
    * Function that Disconnects Server and all clients 
    */
    public void Disconnect()
    {
        try 
        {
            ta_chat.append("Disconnected.\n");  // Appends message to textarea
            tellEveryone("Server&is stopping and all users will be disconnected&Chat"); // Calls function that broadcasts message to everyone
            tellEveryone("Server&is Disconnected&Done");    // Calls function that broadcasts message to everyone
            serverSock.close(); // Closes socket connection
            open = false;   // Means server is no longer open
            tf_port.setEditable(true);  // Sets port textfield to editable
            users.clear();  // Clears list of online users
            b_start.setEnabled(true);   // Re-enables start button
        } catch(IOException ex) {
            ta_chat.append("Failed to disconnect. \n"); // Appends message to textarea
        }
    }
   
    /**
    * Creates new form server_frame
    */
    public server_frame()
    {
        initComponents();
        ta_chat.setEditable(false); // Sets textarea to not editable
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ta_chat = new javax.swing.JTextArea();
        b_start = new javax.swing.JButton();
        b_end = new javax.swing.JButton();
        b_users = new javax.swing.JButton();
        b_clear = new javax.swing.JButton();
        tf_port = new javax.swing.JTextField();
        portlbl = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Chat - Server's frame");
        setBackground(new java.awt.Color(0, 0, 102));
        setName("server"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        ta_chat.setColumns(20);
        ta_chat.setRows(5);
        jScrollPane1.setViewportView(ta_chat);

        b_start.setBackground(new java.awt.Color(255, 255, 0));
        b_start.setFont(new java.awt.Font("Charlemagne Std", 0, 11)); // NOI18N
        b_start.setForeground(new java.awt.Color(0, 0, 102));
        b_start.setText("START Server");
        b_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_startActionPerformed(evt);
            }
        });

        b_end.setBackground(new java.awt.Color(255, 255, 0));
        b_end.setFont(new java.awt.Font("Charlemagne Std", 0, 11)); // NOI18N
        b_end.setForeground(new java.awt.Color(0, 0, 102));
        b_end.setText("Stop Server");
        b_end.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_endActionPerformed(evt);
            }
        });

        b_users.setBackground(new java.awt.Color(255, 255, 0));
        b_users.setFont(new java.awt.Font("Charlemagne Std", 0, 11)); // NOI18N
        b_users.setForeground(new java.awt.Color(0, 0, 102));
        b_users.setText("Online Users");
        b_users.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_usersActionPerformed(evt);
            }
        });

        b_clear.setBackground(new java.awt.Color(255, 255, 0));
        b_clear.setFont(new java.awt.Font("Charlemagne Std", 0, 11)); // NOI18N
        b_clear.setForeground(new java.awt.Color(0, 0, 102));
        b_clear.setText("Clear");
        b_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_clearActionPerformed(evt);
            }
        });

        tf_port.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_portActionPerformed(evt);
            }
        });

        portlbl.setFont(new java.awt.Font("Charlemagne Std", 0, 11)); // NOI18N
        portlbl.setForeground(new java.awt.Color(0, 0, 102));
        portlbl.setText("Port:");

        jLabel1.setFont(new java.awt.Font("Charlemagne Std", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 102));
        jLabel1.setText("Share & Discuss Server");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(b_end, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(b_start, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(b_clear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(b_users, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(184, 184, 184)
                .addComponent(portlbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tf_port, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_start)
                    .addComponent(b_users))
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(portlbl)
                    .addComponent(tf_port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_clear)
                    .addComponent(b_end))
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Button that ends server
     */ 
    private void b_endActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_endActionPerformed
        Disconnect();  // Calls function that disconnects server and all client's 
        ta_chat.append("\nServer stopping... \n");  // Append message to textarea
    }//GEN-LAST:event_b_endActionPerformed

     /**
     * Button that starts server
     */ 
    private void b_startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_startActionPerformed
        String portnum = tf_port.getText(); // Initializes string variable with text entered into textfield
        if (portnum.matches("[0-9]+") && portnum.length() == 4) // If text entered matches the pattern and is of length 4
        {
              port = Integer.parseInt(portnum); // Initializes integer variable with string variable converted to an integer 
              if(port < 9036 || port > 9040 )   // If integer is less than 9001 or greater tha 9069
              {
                  JFrame frame = new JFrame("Error");   // Displays error frame
                  JOptionPane.showMessageDialog(frame, "Please enter a Port Number between 9036 and 9040"); // Displays error message on frame
              }
              else
              {
                  try
                  {
                    tf_port.setEditable(false); // Sets port textfield to not editable
                    Thread starter = new Thread(new ServerStart()); // Creates new thread object that calls class that starts server
                    starter.start();    // Starts thread
                    ta_chat.append("Server started...\n");  // Appends message to textarea
                    open = true;    // Means server is open 
                    b_start.setEnabled(false);  // Sets start button to disabled
                  }
                  catch(NullPointerException ex)
                  {
                      ta_chat.append(ex.getMessage());  // Appends message to textarea             
                  }
              }
        }
        else
        {
            JFrame frame = new JFrame("Error"); // Displays error frame
            JOptionPane.showMessageDialog(frame, "Please enter a Port Number between 9036 and 9040");   // Displays error message on frame
        }
    }//GEN-LAST:event_b_startActionPerformed

     /**
     * Button that displays users online
     */ 
    private void b_usersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_usersActionPerformed
        ta_chat.append("\nOnline users :\n");   // Appends message to textarea
        for (String current_user : users)   // For number of elements in user ArrayList
        {
            ta_chat.append(current_user+"\n");   // Append string variable with username to textarea
        }      
    }//GEN-LAST:event_b_usersActionPerformed

    /**
     * Button that displays users online
     */ 
    private void b_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_clearActionPerformed
        ta_chat.setText("");    // Sets text of textarea to empty
    }//GEN-LAST:event_b_clearActionPerformed

    /**
     * TextField for port number
     */ 
    private void tf_portActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_portActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_portActionPerformed

    /**
     * Actions that occur should server frame be exited
     */ 
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if(open == true)    // If server is still open
        {
            Disconnect();   // Call function to Disoconnect server and all clients
        }
        System.exit(0); // Exits program
    }//GEN-LAST:event_formWindowClosing

    public static void main(String args[]) 
    {
         /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            @Override
            public void run() {
                new server_frame().setVisible(true);
            }
        });
    }
    
    /*
    * Class that starts server
    */
    public class ServerStart implements Runnable 
    {
        @Override
        public void run() 
        {
            clientOutputStreams = new ArrayList();  // Creates new ArrayList Object for list of outputstream
            users = new ArrayList();  // Creates new ArrayList object for list of Users online
           
            try 
            {
                serverSock = new ServerSocket(port);    // Creates new ServerSocket object on port number entered 

                while (true) 
                {
                    Socket clientSock = serverSock.accept();    // Creates socket based on connection serversocket object accepts
                    PrintWriter writer = new PrintWriter(clientSock.getOutputStream()); // Creates PrintWriter object set to write to socket connection output stream
                    clientOutputStreams.add(writer);    // Adds PrintWriter object output stream to list of output streams

                    Thread listener = new Thread(new ClientHandler(clientSock, writer));    // Creates new thread object that calls class to handle clients
                    listener.start();   // Starts thread
                    ta_chat.append("\nGot a connection. \n");   // Appends message to textarea
                }
            }
            catch (IOException ex)
            {
                ta_chat.append("No more Connections\n");    // Appends message to textarea
            }
        }
    }
    
    /*
    * Function that tells everyone online a message
    * Parameter is a string variable
    */
    public void tellEveryone(String message) 
    {
	Iterator it = clientOutputStreams.iterator();   // Creates Iterator object set to iterate through list of output streams

        while (it.hasNext())    // While there is still a output stream to iterate to
        {
            try 
            {
                PrintWriter writer = (PrintWriter) it.next();   // Create new PrintWriter object based on next iterator output stream
		writer.println(message);    // PrintWriter object writes message to client
                writer.flush(); // Flushes PrintWriter object's buffer
            } 
            catch (Exception ex) 
            {
		ta_chat.append("Error telling everyone. \n");   // Append message to textarea
            }
        } 
        ta_chat.append("Sending: " + message + "\n");   // Append message to textarea
        ta_chat.setCaretPosition(ta_chat.getDocument().getLength());    // Sets caret to end of textarea
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_clear;
    private javax.swing.JButton b_end;
    private javax.swing.JButton b_start;
    private javax.swing.JButton b_users;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel portlbl;
    private javax.swing.JTextArea ta_chat;
    private javax.swing.JTextField tf_port;
    // End of variables declaration//GEN-END:variables
}
