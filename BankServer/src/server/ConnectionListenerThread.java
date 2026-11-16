package server;

/**
 * This class creates threads able to listen continuously, on a Server object,
 * for the arrival of new clients.
 */
public class ConnectionListenerThread extends Thread {
    /**
     * Sleep delay in the listening loop.
     */
    public static int SLEEP_DELAY = 100;
    private Server server;

    /**
     * Builds a thread on a server
     *
     * @param s Server the server on which the thread will listen
     */
    public ConnectionListenerThread(Server s) {
        server = s;
    }

    /**
     * Main method of the thread. This method continuously calls the waitForConnection() method of the server.
     */
    public void run() {
        while (!interrupted()) {
            server.waitForConnection();
            try {
                Thread.sleep(SLEEP_DELAY);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
