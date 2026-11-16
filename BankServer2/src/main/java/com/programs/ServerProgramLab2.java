package com.programs;

import com.server.Config;
import com.server.Server;
import com.bank.server.BankServer;

import java.util.Scanner;

/**
 * Simple server demonstration program. The program starts a server that begins listening
 * for the arrival of connections.
 */
public class ServerProgramLab2 {
	/**
	 * Main method of the program.
	 *
	 * @param args Program arguments
	 */
    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);
        String input;

        Server server = new BankServer(Config.SERVER_PORT);
        if (server.start()) {
            System.out.println("Server listening on port " + server.getPort());
        }

        System.out.println("Type EXIT to stop the server.");
        input = keyboard.nextLine();
        while (!"EXIT".equals(input)) {
            System.out.println("??? Type EXIT to stop the server.");
            input = keyboard.nextLine();
        }
        server.stop();
    }
}
