package server.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
/**
 * This class represents a connection point from a client to a server or from a server to a client.
 * It wraps the socket used for the connection as well as the character streams used to send and receive text.
 */
public class Connection {

	private Socket 				socket;
	private PrintWriter 		os;
	private BufferedInputStream is;
	/**
	 * Builds a connection on a socket, initializing the character streams used by the socket.
	 *
	 * @param s Socket the socket on which the connection is created
	 */
	public Connection(Socket s)
	{
		try
		{
		  socket = s;
		  is = new BufferedInputStream(socket.getInputStream());
		  os = new PrintWriter(socket.getOutputStream());
		}
		catch(IOException e)
		{
			throw new IllegalStateException("I/O problem preventing the creation of the Connection object.");
		}
	}
	/**
	 * Checks whether text has arrived on the connection and returns it. Returns the empty string if there is no text.
	 *
	 * @return String the received text, or the empty string if no text arrived.
	 */
	public String getAvailableText()
	{
		String t = "";
		try
		 {
			byte buf[] = new byte[2000];	//read buffer

			if (is.available()<=0)
				return "";
			//Read the input stream
			is.read(buf);
			t = (new String(buf)).trim();
			//Clear the buffer
			buf=null;
		}
		catch(IOException e)
		{
		}
		return t;
	}
	/**
	 * Sends a text on the connection
	 *
	 * @param text String text to send
	 */
	public void send(String text)
	 {
		os.print(text);
		os.flush();
	 }
	/**
	 * Closes the connection by closing the socket and the streams used.
	 *
	 * @return true if the connection was closed correctly, false otherwise.
	 */
	public boolean close()
	{
		try
		{
		  is.close();
		  os.close();
		  socket.close();
		}
		catch(IOException e)
		{
			return false;
		}
		return true;
	}
}
