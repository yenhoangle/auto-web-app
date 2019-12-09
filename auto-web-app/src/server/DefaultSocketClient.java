/*
 * Yen Le
 * 20123455
 *
 * DefaultSocketClient.java
 * Class which handles input from the client and sends objects back to the client.
 * */

package server;
import java.net.*;
import java.io.*;


public class DefaultSocketClient extends Thread {


    ////////// PROPERTIES //////////

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket clientSocket;
    private BuildCarModelOptions protocol;
    private boolean DEBUG=true;

    ////////// CONSTRUCTORS //////////

    public DefaultSocketClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    ////////// INSTANCE METHODS //////////

    public void handleConnection(Socket sock) {
        if (DEBUG)
            System.out.println("Creating server object streams ... ");
        try {
            out = new ObjectOutputStream(sock.getOutputStream());
            in = new ObjectInputStream(sock.getInputStream());
        }
        catch (IOException e) {
            System.err.println("Error creating server object streams ... ");
            System.exit(1);
        }


        protocol = new BuildCarModelOptions();
        String menu = "\nEnter 1 to upload a new Automotive\n"
                + "Enter 2 to configure an Automotive\n"
                + "Enter 0 to terminate connection\n";

        try {
            do {
                if (DEBUG)
                    System.out.println("Sending client interaction choices ... ");
                sendOutput(menu);

                if (DEBUG)
                    System.out.println("Reading client request ... ");
                int request = Integer.parseInt(in.readObject().toString());
                if (DEBUG)
                    System.out.println(request);
                if (request == 0)
                    break;

                if (DEBUG)
                    System.out.println("Sending client request follow-up ... ");
                sendOutput(protocol.setRequest(request));

                if (request >= 1 && request <= 2)
                    handleInput(request);

            } while (in.readObject() != null);

            if (DEBUG)
                System.out.println("Closing server input stream for client " + sock.getInetAddress() + " ... ");
            in.close();
        }
        catch (IOException e) {
            System.err.println("Error handling client connection ... ");
            System.exit(1);
        }
        catch (ClassNotFoundException e) {
            System.err.println("Error in uploaded object, object may be corrupted ... ");
            System.exit(1);
        }
    }

    public void sendOutput(Object obj) {
        try {
            out.writeObject(obj);
        }
        catch (IOException e) {
            System.err.println("Error returning output to client ... ");
            System.exit(1);
        }
    }

    public void handleInput(int request) {
        Object fromClient;
        Object toClient;
        try {
            switch (request) {
                case 1: //Client request to build Automotive
                    if (DEBUG)
                        System.out.println("Waiting for client to upload file ... ");
                    fromClient = in.readObject();
                    if (DEBUG) {
                        System.out.println(fromClient);
                        System.out.println("Adding new Automotive to database ... ");
                    }
                    toClient = protocol.processRequest(fromClient);
                    if (DEBUG) {
                        System.out.println("Sending back to client: " + toClient);
                    }
                    sendOutput(toClient);
                    break;

                case 2: //Client request to configure Automobile
                    if (DEBUG)
                        System.out.println("Waiting for client to select Automotive ... ");
                    fromClient = in.readObject().toString();
                    if (DEBUG)
                        System.out.println("Sending Automotive object to client ... ");
                    toClient = protocol.processRequest(fromClient);
                    sendOutput(toClient);
                    break;

                default: //Invalid requests
                    ;
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error in uploaded object, file may be corrupted ... ");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error in retrieving object from client ... ");
            System.exit(1);

        }
    }

    @Override
    public void run() {
        handleConnection(clientSocket);

        //Close client output stream
        if (DEBUG)
            System.out.println("Closing server output stream for client " + clientSocket.getInetAddress() + " ... ");
        try {
            out.close();
        }
        catch (IOException e) {
            System.err.println("Error closing server output stream for client " + clientSocket.getInetAddress() + " ... ");
        }

        //Close client socket
        if (DEBUG)
            System.out.println("Closing client socket " + clientSocket.getInetAddress() + " ... ");
        try {
            clientSocket.close();
        }
        catch (IOException e) {
            System.err.println("Error closing client socket " + clientSocket.getInetAddress() + " ... ");
        }
    }

}
