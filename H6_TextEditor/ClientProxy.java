
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClientProxy implements Observer {

    //need reference to Server
    private ServerInterface server;

    //singleton pattern is used to ensure that every server object is only handled by one IO object (this)
    private static ClientProxy instance = null;

    private int port = 55555;

    //a list of all addresses used by the clients listening to this server
    private ArrayList <UpdateAddress> clientAddresses;

    private ClientProxy (){
        server = new Server(new SourceText());
        server.attach(this);
        clientAddresses = new ArrayList <UpdateAddress> ();
    }

    public static ClientProxy getInstance (){

        if(instance == null){
            instance = new ClientProxy();
        }
        
        return instance;
    }

    public void begin () throws Exception {

        System.out.println("This Address: " + Inet4Address.getLocalHost().toString() + ", Port: " + port);

        ServerSocket welcomeSocket = new ServerSocket(port);

        while (true){

            //block the server until a connection is created
            //then create a new socket to transfer data
            Socket serverSocket = welcomeSocket.accept();

            //System.out.println(serverSocket.getRemoteSocketAddress());

            //we can use outToClient to write to the socket
            DataOutputStream outToClient = new DataOutputStream(serverSocket.getOutputStream());

            //we can use inFromClient to read in to the socket
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

            //read the request from the client
            String requestFromClient = inFromClient.readLine();

            //print out the request to the screen
            //System.out.println("Client Request: " + requestFromClient);

            requestFromClient = new String(requestFromClient.replace("~~~", "\n"));

            //break the string into tokens separated by the ':'
            String[] tokens = requestFromClient.split(":::");

            //enforce a protocol- there must be three values in the request string
            if(tokens.length > 1 && (tokens[1].equalsIgnoreCase("set") || tokens[2].equalsIgnoreCase("get") || tokens[2].equalsIgnoreCase("detach"))){
                
                //if this is a set request we take the input text and send it to the server
                if(tokens[1].equals("set")){

                    //empty string set
                    if(tokens.length == 2){
                        server.setText("");
                    }
                    //set the text to whatever the text in request is 
                    else {
                        server.setText(tokens[2]);
                    }
                }
                //otherwise, we are getting the server's text or detaching a client
                else {
                    //an address object used to hold and match against user addresses
                    UpdateAddress u = new UpdateAddress(tokens[0], Integer.parseInt(tokens[1]), (InetSocketAddress)serverSocket.getRemoteSocketAddress());

                    //the index of the address in the list, if found
                    int index = findAddress(u);

                    if(tokens[2].equals("detach")){
                        //remove this address only if it is found
                        if(index >= 0){
                            System.out.println("Detaching: " + u.toString());
                            clientAddresses.remove(index);
                            //System.out.println(clientAddresses.toString());
                        }
                        else {
                            System.out.println("Address " + u.toString() + "Not Found, Cannot Detach");
                        }
                    }
                    else {
                        //add this address only if it is new
                        if(index == -1){
                            System.out.println("Adding Address " + u.toString());
                            clientAddresses.add(u);
                        }

                        //System.out.println("Get Request Recieved");

                        //get the text and send it back up the chain
                        //write the result back to the server proxy

                        String serverString = ((Server)server).getText();
                        serverString = new String(serverString.replace("\n", "~~~"));

                        outToClient.writeBytes(serverString + "\n");
                    }
                }
            }
            else
            {
                //invalid message
                outToClient.writeBytes("Could not Fulfill Request\n");
            }

            //close the socket
            serverSocket.close();

        }
    }

    private int findAddress (UpdateAddress u){

        //if the address is found, we set the return to the index it is at, otherwise it is -1 (not here)
        int found = -1;
        int index = 0;

        //try to find the ip port combination to prevent sending multiple update messages to the same client
        for(UpdateAddress find : clientAddresses){
            if((find.getIP().equals(u.getIP())) && (find.getPort() == u.getPort())){
                found = index;
                break;
            }
            index++;
        }

        return found;
    }

    public void update (){
        
        //update all the clients about the most recent copy of the source text

        //network updates to ServerProxy
        //System.out.println("Update message: Client Proxy");
        
        //network classes throw exceptions
        try 
        {
            //for every client we keep track of, send an update message 
            for (UpdateAddress clientAddress : clientAddresses){

                //System.out.println("Updating " + clientAddress.getIP() + ": " + clientAddress.getPort());

                Socket clientSocket = new Socket(clientAddress.getFullAddress().getAddress(), clientAddress.getPort());

                //we can use outToServer to write to the socket
                DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

                //we can use inFromServer to read in to the socket
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                //create the request to send to the other proxy
                String request = ":::UPDATE MESSAGE FROM SERVER:::";
                outToClient.writeBytes(request + "\n");
                clientSocket.close();
            }
        }

        catch(Exception e)
        {
            e.printStackTrace();
            System.err.println("Error Updating Clients");
        }
          
    }

}

