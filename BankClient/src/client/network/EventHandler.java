package client.network;

/**
 * This interface represents an event handler.
 */
@FunctionalInterface
public interface EventHandler {
	/**
	* Event handling method.
	*
	* @param event the event to handle.
	*/
	void handle(Event event);
}
