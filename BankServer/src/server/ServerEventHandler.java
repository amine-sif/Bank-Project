package server;

import bank.Bank;
import bank.BankAccount;
import bank.ClientAccount;
import bank.AccountType;
import server.network.Event;
import server.network.EventHandler;
import server.network.Connection;

import java.util.List;

/**
 * This class represents a server event handler. When a server receives text from a client,
 * it creates an event from the received text and notifies this handler, which reacts by handling the event.
 */
public class ServerEventHandler implements EventHandler {
    private Server server;

    /**
     * Builds an event handler for a server.
     *
     * @param server Server the server for which this handler handles events
     */
    public ServerEventHandler(Server server) {
        this.server = server;
    }

    /**
     * Event handling method. It holds the code that handles the requests received from a client.
     *
     * @param event the event to handle.
     */
    @Override
    public void handle(Event event) {
        Object source = event.getSource();
        BankServer bankServer = (BankServer)server;
        Bank bank;
        BankConnection cnx;
        String msg, eventType, argument, clientAccountNumber, pin, accountNumber, str;
        String[] t;
        ClientAccount clientAccount;
        BankAccount bankAccount;
        boolean alreadyConnected;
        List<BankAccount> accounts;
        double  amount, balance;

        if (source instanceof Connection) {
            cnx = (BankConnection) source;
            System.out.println("SERVER: Received : " + event.getType() + " " + event.getArgument());
            eventType = event.getType();
            cnx.setLastOperationTime(System.currentTimeMillis());
            switch (eventType) {
                /******************* GENERAL COMMANDS *******************/
                case "EXIT": //Closes the connection with the client that sent "EXIT":
                    cnx.send("END");
                    bankServer.remove(cnx);
                    cnx.close();
                    break;
                case "LIST": //Sends the list of connected client account numbers:
                    cnx.send("LIST " + bankServer.list());
                    break;
                /******************* ACCOUNT MANAGEMENT COMMANDS *******************/
                case "NEW": //Creates a new client account:
                    if (cnx.getClientAccountNumber()!=null) {
                        cnx.send("NEW NO already connected");
                        break;
                    }
                    argument = event.getArgument();
                    t = argument.split(":");
                    if (t.length<2) {
                        cnx.send("NEW NO");
                    }
                    else {
                        clientAccountNumber = t[0];
                        pin = t[1];
                        bank = bankServer.getBank();
                        if (bank.add(clientAccountNumber,pin)) {
                            cnx.setClientAccountNumber(clientAccountNumber);
                            accountNumber = bank.getDefaultAccountNumber(clientAccountNumber);
                            cnx.setCurrentAccountNumber(accountNumber);
                            bankAccount = bank.getBankAccountByNumber(accountNumber);
                            cnx.send("NEW OK " + bankAccount.getNumber()+"[CHECKING] "+bankAccount.getBalance());
                        }
                        else
                            cnx.send("NEW NO ");
                    }
                    break;
                case "CONNECT": //Question 3.1 : Login to the client account
                    //The client is prevented from logging in to several client accounts at the
                    //same time (NOT required by the lab):
                    if (cnx.getClientAccountNumber()!=null) {
                        cnx.send("CONNECT NO");
                        break;
                    }
                    //Get the number and the PIN:
                    argument = event.getArgument();
                    t = argument.split(":");
                    if (t.length<2) {
                        cnx.send("CONNECT NO");
                    }
                    else {
                        clientAccountNumber = t[0];
                        pin = t[1];
                        //Check that the client is not already connected to this account:
                        t = bankServer.list().split(":");
                        alreadyConnected = false;
                        for (String s : t) {
                            if (s.trim().equals(clientAccountNumber)) {
                                alreadyConnected = true;
                            }
                        }
                        if (alreadyConnected) {
                            cnx.send("CONNECT NO");
                            break;
                        }
                        //Check that the client account exists:
                        bank = bankServer.getBank();
                        clientAccount = bank.getClientAccount(clientAccountNumber);
                        if (clientAccount==null) {
                            cnx.send("CONNECT NO");
                            break;
                        }
                        //Check that the PIN is correct:
                        if (!pin.equals(clientAccount.getPin())) {
                            cnx.send("CONNECT NO");
                            break;
                        }
                        //Everything is OK. Record the client account number and its checking account number
                        //in the connection:
                        cnx.setClientAccountNumber(clientAccountNumber);
                        accounts = clientAccount.getAccounts();
                        for (BankAccount acc:accounts) {
                            if (acc.getType()== AccountType.CHECKING) {
                                accountNumber = acc.getNumber();
                                cnx.setCurrentAccountNumber(accountNumber);
                                str = "";
                                acc = bank.getClientAccount(clientAccountNumber).getCheckingAccount();
                                if (acc!=null)
                                    str += acc.getNumber()+"[CHECKING] "+acc.getBalance();
                                acc = bank.getClientAccount(clientAccountNumber).getSavingsAccount();
                                if (acc!=null)
                                    str += ":"+acc.getNumber()+"[SAVINGS] "+acc.getBalance();
                                cnx.send("CONNECT OK "+str);
                                break;
                            }
                        }
                    }
                    break;
                case "SAVINGS" : //Question 4.2 :
                    //Check that the client is already logged in to their client account:
                    clientAccountNumber = cnx.getClientAccountNumber();
                    if (clientAccountNumber==null) {
                        cnx.send("SAVINGS NO");
                        break;
                    }
                    //
                    bank = bankServer.getBank();
                    if (bank.addSavingsAccount(clientAccountNumber)) {
                        cnx.send("SAVINGS OK "+bank.getClientAccount(clientAccountNumber).getSavingsAccount().getNumber());
                    }
                    else {
                        cnx.send("SAVINGS NO");
                    }
                    break;
                case "SELECT" : //Question 5.1 :
                    //Check that the client is already logged in to their client account:
                    clientAccountNumber = cnx.getClientAccountNumber();
                    if (clientAccountNumber==null) {
                        cnx.send("SELECT NO");
                        break;
                    }
                    argument = event.getArgument(); //type of account to select ("checking" or "savings")
                    accountNumber = bankServer.getBank().getBankAccountNumber(clientAccountNumber,argument);
                    if (accountNumber==null) {
                        cnx.send("SELECT NO");
                    }
                    else  {
                        bankAccount = bankServer.getBank().getBankAccountByNumber(accountNumber);
                        if (bankAccount!=null)
                            balance = bankAccount.getBalance();
                        else
                            balance = 0;
                        cnx.setCurrentAccountNumber(accountNumber);
                        cnx.send("SELECT OK "+accountNumber+" "+balance);
                    }
                    break;
                case "DEPOSIT": //Question 6.1 :
                    //Check that the client is already logged in to their client account:
                    clientAccountNumber = cnx.getClientAccountNumber();
                    if (clientAccountNumber==null) {
                        cnx.send("DEPOSIT NO");
                        break;
                    }
                    argument = event.getArgument(); //deposit amount as a String
                    try {
                        amount = Double.parseDouble(argument);
                        if (bankServer.getBank().deposit(amount,cnx.getCurrentAccountNumber())) {
                            balance = bankServer.getBank().getBankAccountByNumber(cnx.getCurrentAccountNumber()).getBalance();
                            cnx.send("DEPOSIT OK "+balance);
                        }
                        else {
                            cnx.send("DEPOSIT NO");
                        }
                    }
                    catch (NumberFormatException exp) {
                        cnx.send("DEPOSIT NO");
                    }
                    break;
                case "WITHDRAW": //Question 6.2 :
                    //Check that the client is already logged in to their client account:
                    clientAccountNumber = cnx.getClientAccountNumber();
                    if (clientAccountNumber==null) {
                        cnx.send("WITHDRAW NO");
                        break;
                    }
                    argument = event.getArgument(); //withdrawal amount as a String
                    try {
                        amount = Double.parseDouble(argument);
                        if (bankServer.getBank().withdraw(amount,cnx.getCurrentAccountNumber())) {
                            balance = bankServer.getBank().getBankAccountByNumber(cnx.getCurrentAccountNumber()).getBalance();
                            cnx.send("WITHDRAW OK "+balance);
                        }
                        else {
                            cnx.send("WITHDRAW NO");
                        }
                    }
                    catch (NumberFormatException exp) {
                        cnx.send("WITHDRAW NO");
                    }
                    break;
                case "BILL": //Question 6.3 :
                    //Check that the client is already logged in to their client account:
                    clientAccountNumber = cnx.getClientAccountNumber();
                    if (clientAccountNumber==null) {
                        cnx.send("BILL NO");
                        break;
                    }
                    argument = event.getArgument();
                    t = argument.split(" ");
                    if (t.length<3) { //Not enough arguments in the command
                        cnx.send("BILL NO");
                        break;
                    }
                    try {
                        amount = Double.parseDouble(t[0]);
                        if (bankServer.getBank().payBill(amount,cnx.getCurrentAccountNumber(),t[1],
                                argument.substring(argument.indexOf(t[1])+t[1].length()).trim())) {
                            balance = bankServer.getBank().getBankAccountByNumber(cnx.getCurrentAccountNumber()).getBalance();
                            cnx.send("BILL OK "+balance);
                        }
                        else {
                            cnx.send("BILL NO");
                        }
                    }
                    catch (NumberFormatException exp) {
                        cnx.send("BILL NO");
                    }
                    break;
                case "TRANSFER": //Question 6.4 :
                    //Check that the client is already logged in to their client account:
                    clientAccountNumber = cnx.getClientAccountNumber();
                    if (clientAccountNumber==null) {
                        cnx.send("TRANSFER NO");
                        break;
                    }
                    argument = event.getArgument();
                    t = argument.split(" ");
                    if (t.length<2) { //Not enough arguments in the command
                        cnx.send("TRANSFER NO");
                        break;
                    }
                    try {
                        amount = Double.parseDouble(t[0]);
                        if (bankServer.getBank().transfer(amount,cnx.getCurrentAccountNumber(),t[1])) {
                            balance = bankServer.getBank().getBankAccountByNumber(cnx.getCurrentAccountNumber()).getBalance();
                            cnx.send("TRANSFER OK "+balance);
                        }
                        else {
                            cnx.send("TRANSFER NO");
                        }
                    }
                    catch (NumberFormatException exp) {
                        cnx.send("TRANSFER NO");
                    }
                    break;
                case "HISTORY":
                    if (cnx.getClientAccountNumber()==null || cnx.getCurrentAccountNumber()==null) {
                        cnx.send("HISTORY NO");
                        break;
                    }
                    bankAccount = bankServer.getBank().getBankAccountByNumber(cnx.getCurrentAccountNumber());
                    str = bankAccount.getHistory();
                    cnx.send("HISTORY "+str);
                    break;
                /******************* DEFAULT HANDLING *******************/
                default: //Send back the received text converted to upper case:
                    msg = (event.getType() + " " + event.getArgument()).toUpperCase();
                    cnx.send(msg);
            }
            bankServer.getBank().print();
        }
    }
}
