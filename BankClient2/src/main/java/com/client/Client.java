/*
 Client states:
DISCONNECTED : the client is disconnected
SEARCHING : the client is looking for the server
NOTFOUND : the client did not find the server
CONNECTING : the server was found but the client waits for the server to validate the request (user + password)
REFUSED : the server refused the connection because the user or password is incorrect
CONNECTED : the client is connected
DISCONNECTING : the client is disconnecting
*/

package com.client;

import java.net.Socket;
import java.io.*;

import com.commonclient.net.Connection;
import com.commonclient.thread.TextReader;
import com.commonclient.event.Event;
import com.commonclient.event.EventHandler;
import com.commonclient.event.EventUtil;
import com.commonclient.thread.TextListenerThread;
import com.observer.Observable;

/**
 * This class represents a client able to connect to a server.
 */
public class Client extends Observable implements TextReader {

    private String serverAddress = Config.SERVER_ADDRESS;
    private int serverPort = Config.SERVER_PORT;
    private boolean connected;
    private Connection connection;
    private EventHandler eventHandler;
    private TextListenerThread textListener;

    /**
     * Provides another event handler to replace the one built by default.
     *
     * @param eventHandler EventHandler the new event handler
     */
    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    /**
     * Connects the client to the server using a socket. If the connection succeeds, a Connection object
     * is created, which creates the input/output streams used to exchange text with the server.
     *
     * @return boolean true if the connection succeeded. false if the connection fails
     * or if the client was already connected.
     */
    public boolean connect() {
        boolean result = false;
        if (this.isConnected()) //already connected
            return result;

        try {
            Socket socket = new Socket(serverAddress, serverPort);
            connection = new Connection(socket);

            //Start the text listener thread:
            textListener = new TextListenerThread(this);
            textListener.start();  //the run() method of the text listener runs in parallel with the rest of the program.
            result = true;
            this.setConnected(true);
        } catch (IOException e) {
            this.disconnect();
        }
        return result;
    }

    /**
     * Disconnects the client, if connected, by closing the Connection object. The text "EXIT" is sent to the
     * server to tell it about the disconnection. The text listener thread is stopped.
     *
     * @return boolean true if the client disconnected, false if it was already disconnected
     */
    public boolean disconnect() {
        if (!isConnected())
            return false;

        connection.send("EXIT");
        connection.close();
        if (textListener != null)
            textListener.interrupt();
        this.setConnected(false);
        return true;
    }

    /**
     * This method checks whether text is arriving on the client's connection and, if so, creates an event
     * containing the text data and asks the client event handler to process it.
     */
    public void read() {

        String[] t;
        Event event;
        String text = connection.getAvailableText();

        if (!"".equals(text)){
            t = EventUtil.extractEventInfo(text);
            event = new Event(connection,t[0],t[1]);
            eventHandler.handle(event);
        }
    }

    /**
     * Returns the IP address of the server this client connects to.
     *
     * @return String the IP address of the server, in the format "192.168.25.32"
     */
    public String getServerAddress() {
        return serverAddress;
    }
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * Indicates whether the client is connected to a server.
     *
     * @return boolean true if the client is connected, false otherwise
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Marks this client as connected or disconnected.
     *
     * @param connected boolean If true, marks the client as connected; if false, as disconnected
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
        this.notifyObservers();
    }

    /**
     * Returns the listening port of the server this client connects to.
     *
     * @return int listening port of the server
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * Specifies the listening port of the server this client connects to.
     *
     * @param serverPort int listening port of the server
     */
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * Sends a text to the server using a Connection object.
     *
     * @param s String text to send
     */
    public void send(String s) {
        this.connection.send(s);
    }
}
