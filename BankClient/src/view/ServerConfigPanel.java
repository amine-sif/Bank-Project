package view;

import javax.swing.*;
import java.awt.*;


public class ServerConfigPanel extends JPanel {
    private JTextField txtServerAddress, txtPortNumber;

    public ServerConfigPanel(String address, int port) {
        //Creation and initialization of the components:
        txtServerAddress = new JTextField(address);
        txtPortNumber = new JTextField(String.valueOf(port));

        //Panel configuration
        setLayout(new GridLayout(2,2));
        add(new JLabel("IP address : "));
        add(txtServerAddress);
        add(new JLabel("Port : "));
        add(txtPortNumber);
    }
    public String getServerAddress() {
        return txtServerAddress.getText();
    }
    public String getServerPort() {
        return txtPortNumber.getText();
    }
}
