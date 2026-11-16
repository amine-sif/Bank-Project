package com.programs;

import com.client.Client;
import com.client.ClientEventHandler;

import java.util.Scanner;

/**
 * Simple client demonstration program. The program uses a client to connect to a server
 * and sends it the texts typed by the user until the user types the text EXIT.
 */
public class ClientProgramLab2 {

	/**
	 * Main method of the program.
	 *
	 * @param args Program arguments
	 */
	public static void main(String[] args) {

		Scanner keyboard = new Scanner(System.in);
		Client client = new Client();
		String input;
		client.setEventHandler(new ClientEventHandler(client));

		if (!client.connect()) {
			System.out.println("Server not found at address " + client.getServerAddress()
					+ " on port " + client.getServerPort());
			return;
		}
		System.out.println("You are connected to the server at address " + client.getServerAddress()
				+ " on port " + client.getServerPort());
		System.out.println("The server sends your text back converted to upper case");

		System.out.println("Type your texts (EXIT to quit) :");
		do {
			input = keyboard.nextLine();
			client.send(input);
		}while (!"EXIT".equals(input));
		//When the client sends "EXIT", the server replies "END" and the event handler disconnects the client
	}
}
