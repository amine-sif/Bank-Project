package server;

public class InactiveConnectionsThread extends Thread {
    public static int SLEEP_DELAY = 1000;
    private BankServer bankServer;

    public InactiveConnectionsThread(BankServer bankServer) {
        this.bankServer = bankServer;
    }

    /**
     * Asks the bank server to remove the inactive connections, every SLEEP_DELAY milliseconds.
     */
    @Override
    public void run() {
        while (!interrupted()) {
            bankServer.removeInactive();//Removes all inactive connections.
            try {
                Thread.sleep(SLEEP_DELAY);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
