package controller;

import client.Client;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AccountListListener extends MouseAdapter {

    private Client client;
    public AccountListListener(Client client) {
        this.client = client;
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
        //to be completed
    }
}
