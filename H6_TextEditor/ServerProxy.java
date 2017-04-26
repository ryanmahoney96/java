
import java.io.*;
import java.net.*;
import java.util.Random;

import javax.swing.JOptionPane;

public class ServerProxy extends Subject implements ServerInterface {

    private String ip;

    //this port is used by the clients and the server to transfer the text updates via socket
    private int port = 55555;

    //the port number this client will be listening on for server update messages
    private int listenPort;

    private Thread t;

    public ServerProxy () {
        //get the ip from the user
        ip = JOptionPane.showInputDialog(null, "Enter the IP Address of the Server");

        try {
            //generate a random ip to prevent two clients from taking the same port number (not necessary when using multiple machines)
            Random r = new Random();
            listenPort = r.nextInt(50000) + 1024;
            final ServerSocket updateNotificationSocket = new ServerSocket(listenPort);

            //begin a new thread which will run independently and listen for the server to send update messages
            t = new Thread (new Runnable() {

                @Override
                public void run (){
                   
                   //contantly listen for the server to send update messages
                    while (true){
                        try {
                            Socket receiveSocket = updateNotificationSocket.accept();

                            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(receiveSocket.getInputStream()));

                            String dataFromServer = inFromServer.readLine();

                            //System.out.println("Update message Recieved from Serverside: " + dataFromServer);

                            //notify all the editor windows about the update
                            notifyObservers();

                            //close the socket
                            receiveSocket.close();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            System.err.println("Error in Client Listener: 1A");
                        }
                    }
                }
            });

            t.start();
        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println("Error in Client Listener: 1B");
        }
    }

    public String getText (){
        //use network to recieve the text held by the server

        //default in case of network error
        String response = "Unable to Obtain Text";

        //network classes throw exceptions
        try 
        {
            //System.out.println(ip + ": " + port);

            //create a socket connection with a server on another machine
            Socket clientSocket = new Socket(ip, port);

            //in order to write to the socket we have to
            //create a stream of characters in and out

            //we can use outToServer to write to the socket
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            //we can use inFromServer to read in to the socket
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //create the request to send to the other proxy
            String tempHost = Inet6Address.getLocalHost().toString();
            
            System.out.println(clientSocket.getInetAddress());

            System.out.println(tempHost);

            tempHost = tempHost.substring(tempHost.lastIndexOf('/') + 1);
            String request = tempHost + ":::" + listenPort + ":::get:::";

            //print out the request
            //System.out.println("Request: " + request);

            //send a request to the server- must end with a newline
            //but the newline will not be sent
            outToServer.writeBytes(request + "\n");

            //wait for a response from the server- block until there is a response
            response = inFromServer.readLine();

            //I chose to replace all newline characters to "~~~" because it is a rarely used character sequence and allows us to use newlines in the editors
            response = new String(response.replace("~~~", "\n"));

            //close the socket after we are done with it
            clientSocket.close();

        }

        catch(Exception e)
        {
            e.printStackTrace();
            System.err.println("Error getting Source Text from Server");
        }

        return response;
    }

    @Override
    //network over to ClientProxy
    public void setText(String setString)
    {

        //request string to send to server
        String request;

        //network classes throw exceptions
        try 
        {
            //create a socket connection with a server on another machine
            Socket clientSocket = new Socket(ip, port);

            //in order to write to the socket we have to
            //create a stream of characters in and out

            //we can use outToServer to write to the socket
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            //we can use inFromServer to read in to the socket
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String tempString = setString.replace("\n", "~~~");

            //create the request to send to the other proxy
            String tempIP = Inet6Address.getLocalHost().toString();
            tempIP = tempIP.substring(tempIP.lastIndexOf('/') + 1);
            request = tempIP + ":::set:::" + tempString;

            //print out the request
            //System.out.println("Request: " + request);

            //send a request to the server- must end with a newline
            //but the newline will not be sent
            outToServer.writeBytes(request + "\n");

            //close the socket after we are done with it
            clientSocket.close();

        }

        catch(Exception e)
        {
            e.printStackTrace();
            System.err.println("Error Setting the Source Text");
        }
    
    }

    public void closeConnection (){
        //notify the server that we no longer want to receive updates

        //network classes throw exceptions
        try 
        {
            //create a socket connection with a server on another machine
            Socket clientSocket = new Socket(ip, port);

            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String tempHost = Inet6Address.getLocalHost().toString();
            tempHost = tempHost.substring(tempHost.lastIndexOf('/') + 1);

            String request = tempHost + ":::" + listenPort + ":::detach:::";

            outToServer.writeBytes(request + "\n");

            clientSocket.close();
        }

        catch(Exception e)
        {
            e.printStackTrace();
            System.err.println("Error Disengaging from the Server");
        }

    }

}