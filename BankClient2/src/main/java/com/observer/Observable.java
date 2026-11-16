package com.observer;

import java.util.ArrayList;
import java.util.ListIterator;

public class Observable {
	//List of the objects observing this Observable:
	  private ArrayList<Observer> observers = new ArrayList<>();

	  //Adds an observer to the list, if it is not already there:
	  public boolean addObserver(Observer observer) {

		boolean added;

		if (this.observers.contains(observer))
			added = false;
		else {
			this.observers.add(observer);
			added = true;
		}
		return added;
	  }

	  //Asks all the observers to update themselves:
	  public void notifyObservers() {

		 ListIterator<Observer> iterator = this.observers.listIterator();
		 while (iterator.hasNext())
			iterator.next().update(this);
	  }
}
