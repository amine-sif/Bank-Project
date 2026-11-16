package com.controller;

import com.client.Client;
import com.view.LoginPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginListener implements ActionListener {
    private Client client;
    private LoginPanel loginPanel;

    public LoginListener(Client client, LoginPanel loginPanel) {
        this.client = client;
        this.loginPanel = loginPanel;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        String actionName;
        if (source instanceof JButton) {
            actionName = ((JButton)source).getActionCommand();
            switch (actionName) {
                case "NEW":
                    client.send("NEW "+loginPanel.getClientAccountNumber()+":"+loginPanel.getPin());
                    break;
                case "CONNECT":
                    client.send("CONNECT "+loginPanel.getClientAccountNumber()+":"+loginPanel.getPin());
                    break;
            }
            loginPanel.clear();
        }
    }
}
