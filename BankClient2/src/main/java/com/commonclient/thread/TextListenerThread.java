package com.commonclient.thread;
/**
 * This class creates threads able to read continuously from a TextReader object.
 */
public class TextListenerThread extends Thread {
	/**
	 * Sleep delay in the read loop.
	 */
	public static int SLEEP_DELAY = 100;
	private TextReader reader;

	/**
	 * Builds a thread on a reader
	 * @param reader the reader the thread will read from
	 */
	public TextListenerThread(TextReader reader)
	 {
		 this.reader = reader;
	 }

	/**
	 * Main method of the thread. This method continuously calls the read() method of the
	 * reader (client or server)
	 */
	public void run()
	 {
		while (!interrupted())
		{
			reader.read();
			try
			{
			  Thread.sleep(SLEEP_DELAY);
			}
			catch (InterruptedException e)
			{
				break;
			}
		}
	 }
}
