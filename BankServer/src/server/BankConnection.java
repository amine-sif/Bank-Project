package server;

import server.network.Connection;

import java.net.Socket;

public class BankConnection extends Connection {
    private String clientAccountNumber;
    private long lastOperationTime;
    //Number of the account on which operations are currently done:
    private String currentAccountNumber;
    /**
     * Builds a connection on a socket, initializing the character streams used by the socket and the moment
     * of the last operation done by the client using this connection.
     *
     * @param s Socket the socket on which the connection is created
     */
    public BankConnection(Socket s) {
        super(s);
        lastOperationTime = System.currentTimeMillis();
    }

    /**
     * Indicates whether the client using this connection has been inactive for a duration at least equal to
     * delay milliseconds.
     *
     * @param delay the delay in milliseconds
     * @return true if the inactivity duration is greater than delay
     */
    public boolean isInactiveFor(long delay) {
        //Question 1.1 :
        return System.currentTimeMillis()-lastOperationTime>delay;
    }

    /**
     * Returns the moment in milliseconds of the last operation of the client using this connection.
     *
     * @return the moment in milliseconds of the last operation of the client
     */
    public long getLastOperationTime() {
        return lastOperationTime;
    }

    /**
     * Records the moment of the last operation of the client using this connection.
     *
     * @param lastOperationTime the moment in milliseconds of the last operation of the client
     */
    public void setLastOperationTime(long lastOperationTime) {
        this.lastOperationTime = lastOperationTime;
    }

    /**
     * Retrieves the client account number of the client using this connection.
     *
     * @return the client account number of the client
     */
    public String getClientAccountNumber() {
        return clientAccountNumber;
    }

    /**
     * Changes the client account number of the client using this connection.
     *
     * @param clientAccountNumber the client account number of the client
     */
    public void setClientAccountNumber(String clientAccountNumber) {
        this.clientAccountNumber = clientAccountNumber;
    }

    /**
     * Returns the number of the bank account currently selected by the client.
     * @return the number of the bank account currently selected by the client
     */
    public String getCurrentAccountNumber() {
        return currentAccountNumber;
    }

    /**
     * Specifies the number of the bank account selected by the client.
     * @param currentAccountNumber the number of the bank account selected by the client
     */
    public void setCurrentAccountNumber(String currentAccountNumber) {
        this.currentAccountNumber = currentAccountNumber;
    }
}
