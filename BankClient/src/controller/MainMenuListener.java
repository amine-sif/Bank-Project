package controller;

import client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuListener implements ActionListener {
    private Client client;
    private JFrame frame;

    public MainMenuListener(Client client, JFrame frame) {
        this.client = client;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        String action;
        int res;

        if (source instanceof JMenuItem) {
            action = ((JMenuItem)source).getActionCommand();
            switch (action) {
                case "CONNECT":
                    if (!client.isConnected()) {
                        if (!client.connect()) {
                            JOptionPane.showMessageDialog(frame, "The server is not responding");
                            break;
                        }
                    }
                    break;
                case "DISCONNECT":
                    if (!client.isConnected())
                        break;
                    res = JOptionPane.showConfirmDialog(frame, "You are about to disconnect",
                            "Confirm disconnect",
                            JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
                    if (res == JOptionPane.OK_OPTION){
                        client.disconnect();
                    }
                    break;
                case "CONFIGURE":
                    //TODO : complete (question 1.3)
                    if (client.isConnected()) {
                        JPanel serverConfigPanel = new JPanel();
                        serverConfigPanel.setLayout(new GridLayout(2,2));
                        JTextField txtServerAddress = new JTextField(client.getServerAddress());
                        JTextField txtPortNumber = new JTextField(String.valueOf(client.getServerPort()));
                        serverConfigPanel.add(new JLabel("IP address : "));
                        serverConfigPanel.add(txtServerAddress);
                        serverConfigPanel.add(new JLabel("Port : "));
                        serverConfigPanel.add(txtPortNumber);
                        res = JOptionPane.showConfirmDialog(frame, serverConfigPanel,
                                "Configure server",
                                JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
                        if (res == JOptionPane.OK_OPTION){
                            client.setServerAddress(txtServerAddress.getText());
                            client.setServerPort(Integer.parseInt(txtPortNumber.getText()));
                        }
                    }
                    break;
                case "QUIT":
                    if (client.isConnected()) {
                        res = JOptionPane.showConfirmDialog(frame, "You are about to disconnect",
                                "Confirm quit",
                                JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
                        if (res == JOptionPane.OK_OPTION){
                            client.disconnect();
                            System.exit(0);
                        }
                    }
                    else
                        System.exit(0);
                    break;
            }
        }
    }
}
