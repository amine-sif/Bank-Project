package com.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private JTextField txtClientAccountNumber, txtPin;
    private JButton bNew, bConnect;

    public LoginPanel() {
        JLabel lClientAccountNumber = new JLabel("Client account number : "),
                lPin = new JLabel("PIN : ");
        txtClientAccountNumber = new JTextField(30);
        txtPin = new JTextField(30);

        txtClientAccountNumber.setBorder(BorderFactory.createTitledBorder("Client account number : "));
        txtPin.setBorder(BorderFactory.createTitledBorder("PIN : "));

        bNew = new JButton("Create account");
        bConnect = new JButton("Log in");

        bNew.setActionCommand("NEW");
        bConnect.setActionCommand("CONNECT");

        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        JPanel pAll = new JPanel(new GridLayout(3,1));

        p1.add(txtClientAccountNumber);
        p2.add(txtPin);
        p3.add(bNew);
        p3.add(bConnect);

        this.setLayout(new BorderLayout());
        pAll.add(p1);
        pAll.add(p2);
        pAll.add(p3);
        this.add(pAll, BorderLayout.NORTH);
        this.setBorder(BorderFactory.createLineBorder(new Color(0x00000000,true),200));
    }
    public void setListener(ActionListener listener) {
        bNew.addActionListener(listener);
        bConnect.addActionListener(listener);
    }
    public String getClientAccountNumber() {
        return this.txtClientAccountNumber.getText();
    }
    public String getPin() {
        return this.txtPin.getText();
    }
    public void clear() {
        this.txtClientAccountNumber.setText("");
        this.txtPin.setText("");
    }
}
