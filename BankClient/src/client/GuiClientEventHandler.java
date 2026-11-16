package client;

import client.network.Event;
import client.network.EventHandler;
import client.network.Connection;
import view.MainPanel;

import javax.swing.*;

public class GuiClientEventHandler implements EventHandler {
    private Client client;
    private MainPanel mainPanel;

    /**
     * Builds an event handler for a client.
     *
     * @param client Client the client for which this handler handles events
     * @param mainPanel MainPanel the main panel of the graphical interface
     */
    public GuiClientEventHandler(Client client, MainPanel mainPanel) {

        this.client = client;
        this.mainPanel = mainPanel;
        this.client.setEventHandler(this);
    }
    @Override
    public void handle(Event event) {
        Object source = event.getSource();
        String eventType, arg, str;
        String[] t;
        MainFrame frame;

        if (source instanceof Connection) {
            eventType = event.getType();
            switch (eventType) {
                /******************* GENERAL COMMANDS *******************/
                case "END": //The server asks to close the connection
                    client.disconnect(); //Close the connection
                    break;
                /******************* CREATION and LOGIN *******************/
                case "OK":
                    mainPanel.setVisible(true);
                    frame = (MainFrame) mainPanel.getTopLevelAncestor();
                    frame.setTitle(MainFrame.TITLE);
                    break;
                case "NEW":
                    arg = event.getArgument();
                    if (arg.trim().startsWith("NO")) {
                        JOptionPane.showMessageDialog(mainPanel,"New account refused");
                    }
                    else {
                        mainPanel.hideLoginPanel();
                        mainPanel.showClientAccountPanel();
                        str = arg.substring(arg.indexOf("OK")+2).trim();
                        mainPanel.addAccount(str);
                    }
                    break;
                case "CONNECT":
                    arg = event.getArgument();
                    if (arg.trim().startsWith("NO")) {
                        JOptionPane.showMessageDialog(mainPanel,"Login refused");
                    }
                    else {
                        mainPanel.hideLoginPanel();
                        mainPanel.showClientAccountPanel();
                        str = arg.substring(arg.indexOf("OK")+2).trim();
                        t = str.split(":");
                        for (String s:t) {
                            mainPanel.addAccount(s.substring(0,s.indexOf("]")+1));
                        }
                    }
                    break;
                /******************* ACCOUNT SELECTION *******************/
                case "SAVINGS" :
                    arg = event.getArgument();
                    JOptionPane.showMessageDialog(mainPanel,"SAVINGS "+arg);
                    break;
                case "SELECT" :
                    arg = event.getArgument();
                    JOptionPane.showMessageDialog(mainPanel,"SELECT "+arg);
                    break;

                /******************* BANKING OPERATIONS *******************/
                case "DEPOSIT" :
                    arg = event.getArgument();
                    JOptionPane.showMessageDialog(mainPanel,"DEPOSIT "+arg);
                    break;
                case "WITHDRAW" :
                    arg = event.getArgument();
                    JOptionPane.showMessageDialog(mainPanel,"WITHDRAW "+arg);
                    break;
                case "BILL" :
                    arg = event.getArgument();
                    JOptionPane.showMessageDialog(mainPanel,"BILL " + arg);
                    break;
                case "TRANSFER" :
                    arg = event.getArgument();
                    JOptionPane.showMessageDialog(mainPanel,"TRANSFER " + arg);
                    break;
                /******************* DEFAULT HANDLING *******************/
                default:
                    System.out.println("RECEIVED : "+event.getType()+" "+event.getArgument());
            }
        }
    }
}
