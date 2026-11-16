package observer;

public interface Observer {
	//Lets the observer update itself when it is notified by the
	//Observable object it observes:
	public void update(Observable observable);
}
