package com.view;

import com.client.Client;
import com.controller.LoginListener;
import com.controller.AccountListListener;
import com.controller.AccountOperationsListener;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private Client client;
    private LoginPanel loginPanel;
    private JPanel clientAccountPanel;
    private AccountOperationsPanel accountOperationsPanel;

    private DefaultListModel<String> accountNumbers;
    private JList<String> jlAccountNumbers;
    private JDesktopPane desktop;


    public MainPanel(Client client) {
        this.client = client;

        loginPanel = new LoginPanel();
        loginPanel.setListener(new LoginListener(client, loginPanel));

        accountOperationsPanel = new AccountOperationsPanel();
        accountOperationsPanel.setListener(new AccountOperationsListener(client, accountOperationsPanel));

        clientAccountPanel = new JPanel();

        clientAccountPanel.setLayout(new BorderLayout());
        clientAccountPanel.setBackground(Color.WHITE);
        accountOperationsPanel.setBackground(Color.WHITE);

        accountNumbers = new DefaultListModel<>();

        jlAccountNumbers = new JList<>(accountNumbers);
        jlAccountNumbers.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        jlAccountNumbers.setBorder(BorderFactory.createTitledBorder("Bank accounts"));
        jlAccountNumbers.setPreferredSize(new Dimension(250,500));


        clientAccountPanel.add(accountOperationsPanel, BorderLayout.NORTH);
        clientAccountPanel.add(jlAccountNumbers, BorderLayout.WEST);
        //Register the mouse listener:
        jlAccountNumbers.addMouseListener(new AccountListListener(client));

        this.setLayout(new BorderLayout());

        this.add(loginPanel, BorderLayout.NORTH);
        this.add(clientAccountPanel, BorderLayout.CENTER);
        clientAccountPanel.setVisible(false);
    }

    /**
     * Empties the interface elements of the main panel.
     */
    public void clearAll() {
        this.accountNumbers.clear();
        if (this.desktop != null)
            this.desktop.removeAll();
    }
    public void hideLoginPanel() {
        loginPanel.clear();
        loginPanel.setVisible(false);
    }
    public void showLoginPanel() {
        loginPanel.setVisible(true);
    }
    public void hideClientAccountPanel() {
        clientAccountPanel.setVisible(false);
        this.accountNumbers.clear();
    }
    public void showClientAccountPanel() {
        clientAccountPanel.setVisible(true);
    }
    /**
     * Displays an account number in the accounts JList.
     *
     * @param str string containing the account number
     */
    public void addAccount(String str) {
        accountNumbers.addElement(str); }
}
