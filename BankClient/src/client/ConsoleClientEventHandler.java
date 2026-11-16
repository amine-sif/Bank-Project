package client;

import client.network.Event;
import client.network.EventHandler;
import client.network.Connection;

/**
 * This class represents a client event handler. When a client receives text from a server,
 * it creates an event from the received text and notifies this handler, which reacts by handling the event.
 */
public class ConsoleClientEventHandler implements EventHandler {
    private Client client;

    /**
     * Builds an event handler for a client.
     *
     * @param client Client the client for which this handler handles events
     */
    public ConsoleClientEventHandler(Client client) {
        this.client = client;
    }

    /**
     * Event handling method. It holds the code that handles the responses obtained from a server.
     *
     * @param event the event to handle.
     */
    @Override
    public void handle(Event event) {
        Object source = event.getSource();
        Connection cnx;
        String eventType, arg;
        String[] members;
        if (source instanceof Connection) {
            cnx = (Connection) source;
            eventType = event.getType();
            switch (eventType) {
                /******************* GENERAL COMMANDS *******************/
                case "END": //The server asks to close the connection
                    client.disconnect(); //Close the connection
                    System.out.println("\t\t\t. Connection with the server closed");
                    break;
                case "LIST": //The server sent back the list of connected account numbers
                    arg = event.getArgument();
                    members = arg.split(":");
                    System.out.println("\t\t" + members.length + " connected :");
                    for (String s : members)
                        System.out.println("\t\t\t- " + s);
                    break;
                /******************* DEFAULT HANDLING *******************/
                default: //Display the received text:
                    System.out.println("\t\t\t."+event.getType()+" "+event.getArgument());
            }
        }
    }
}
