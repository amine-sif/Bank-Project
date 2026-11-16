package controller;

import client.Client;
import view.AccountOperationsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountOperationsListener implements ActionListener {
    private Client client;

    public AccountOperationsListener(Client client, AccountOperationsPanel accountOperationsPanel) {
        this.client = client;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        String actionName;
        if (source instanceof JButton) {
            actionName = ((JButton)source).getActionCommand();
            switch (actionName) {
                case "SAVINGS":
                    client.send("SAVINGS");
                    break;
            }
        }
    }
}
