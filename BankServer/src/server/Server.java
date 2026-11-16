package server;

import java.util.*;

import server.network.Connection;
import server.network.TextReader;
import server.network.TextListenerThread;
import server.network.Event;
import server.network.EventUtil;
import server.network.EventHandler;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

/**
 * This class represents a server on which clients can connect.
 */
public class Server implements TextReader {

    //List of the clients connected to the server:
    protected final Vector<Connection> connected = new Vector<>();

    //New clients that have not yet "identified" themselves:
    private final Vector<Connection> newcomers = new Vector<>();
    //This thread interacts with the newcomers to validate their connection:
    private Thread newcomersThread;
    private int port = 8888;
    //Thread that waits for new connections:
    private ConnectionListenerThread connectionListener;
    //Thread that listens for the arrival of text from the connected clients:
    private TextListenerThread textListener;
    //The server socket used by the server to wait for clients to connect:
    private ServerSocket serverSocket;
    //Indicates whether the server is already started or not:
    private boolean started;
    //Listener that handles the events corresponding to the arrival of text from clients:
    protected EventHandler eventHandler;

    /**
     * Creates a server that will listen on the specified port.
     *
     * @param port int listening port of the server
     */
    public Server(int port) {
        this.port = port;
    }
    /**
     * Provides another event handler to replace the one built by default.
     *
     * @param eventHandler EventHandler the new event handler
     */
    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }
    /**
     * Starts the server, if it has not already been started. Starts the thread that listens for the arrival
     * of clients and the one that listens for the arrival of text. Sets up the event handler of the server.
     *
     * @return boolean true if the server was started correctly, false if the server was already started or if
     * there is a connection problem
     */
    public boolean start() {
        if (started) //Server already started.
            return false;
        try {
            serverSocket = new ServerSocket(port);
            connectionListener = new ConnectionListenerThread(this);
            connectionListener.start();
            textListener = new TextListenerThread(this);
            textListener.start();
            eventHandler = new ServerEventHandler(this);
            started = true;
            return true;
        } catch (IOException e) {
            System.out.println("serverSocket error : " + e.getMessage());
        }
        return false;
    }

    /**
     * Stops the server by stopping the threads that listen for the arrival of clients, the arrival of text and the
     * handling of new clients.
     */
    public void stop() {
        ListIterator<Connection> iterator;
        Connection cnx;

        if (!started)
            return;
        connectionListener.interrupt();
        textListener.interrupt();
        if (newcomersThread!=null) newcomersThread.interrupt();
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("serverSocket error : " + e.getMessage());
        }
        //All connections are closed after sending "END" to each of the clients:
        iterator = connected.listIterator();
        while (iterator.hasNext()) {
            cnx = iterator.next();
            cnx.send("END");
            cnx.close();
        }
        started = false;
    }

    /**
     * This method blocks on the ServerSocket of the server until a client connects to it. In that case, it
     * creates the connection to this client and adds it to the list of newcomers.
     */
    public void waitForConnection() {
        try {
            Socket sock = serverSocket.accept();
            Connection cnx = new BankConnection(sock);
            newcomers.add(cnx);
            System.out.println("New connection");
            if (newcomersThread == null) {
                newcomersThread = new Thread() {
                    @Override
                    public void run() {
                        Connection connection;
                        ListIterator<Connection> it;
                        boolean checkOK = true;

                        while (!interrupted()) {
                            it = Server.this.newcomers.listIterator();
                            while (it.hasNext()) {
                                connection = it.next();
                                //Check here whether the client identified itself properly, if needed
                                checkOK = validateConnection(connection);
                                if (checkOK) {
                                    connection.send("OK");
                                    it.remove();
                                    Server.this.add(connection);
                                }
                            }
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                break;
                            }
                        }
                    }
                };
                newcomersThread.start();
            }
        } catch (IOException e) {

        }
    }
    /**
     * Validates the arrival of a new client on the server. The implementation depends on the needs of
     * the application. For example, this method can check whether the new client sent the right credentials
     * (name and password).
     * In this lab, this method does nothing. It simply returns true.
     *
     * @param connection Connection the connection representing the client
     * @return boolean true if the client validated its arrival correctly, false otherwise
     */
    protected boolean validateConnection(Connection connection) {
        return true;
    }
    /**
     * Adds the connection of a new client to the list of connected clients.
     * @param connection Connection the connection representing the client
     * @return boolean true if the addition was done successfully, false otherwise
     */
    protected synchronized boolean add(Connection connection) {
        return this.connected.add(connection);
    }
    /**
     * Removes the connection from the list of connected clients.
     * @param connection Connection the connection representing the client
     * @return boolean true if the removal was done successfully, false otherwise
     */
    protected synchronized boolean remove(Connection connection) {
        return this.connected.remove(connection);
    }
    /**
     * This method scans all the clients currently connected to this server to check whether text is
     * arriving. For each text that arrives, it creates an event containing the text data and asks the
     * server event handler to process the event.
     */
    public synchronized void read() {
        Connection cnx;
        String[] t;
        Event event;
        for (int i=0;i<connected.size();i++) {
            cnx = connected.get(i);
            String text = cnx.getAvailableText();
            if (!"".equals(text)) {
                t = EventUtil.extractEventInfo(text);
                event = new Event(cnx, t[0], t[1]);
                eventHandler.handle(event);
            }
        }
    }
    /**
     * Returns the listening port of this server
     *
     * @return int The listening port
     */
    public int getPort() {
        return port;
    }

    /**
     * Specifies the listening port of the server.
     *
     * @param port int The listening port
     */
    public void setPort(int port) {
        this.port = port;
    }
    /**
     * Indicates whether the server has been started.
     *
     * @return boolean true if the server is started and false otherwise
     */
    public boolean isStarted() {
        return started;
    }
}
