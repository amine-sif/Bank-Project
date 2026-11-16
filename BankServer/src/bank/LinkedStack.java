package bank;

import java.io.Serializable;

/**
 * Implements a dynamic stack of objects (singly linked).
 * Operating strategy: LIFO (Last In First Out).
 */

public class LinkedStack<T> implements Serializable {

	private Node top; 		//references the element at the top of the stack

	private int count; 	//Number of elements in the stack (used so that
						//size() does not have to recompute it).
						//Remember to increment it when pushing and decrement it when popping.

	/**
	 * Constructor without parameter
	 * Initializes the top reference to null and count to 0.
	 */
	public LinkedStack(){
		this.top = null;
		this.count = 0;
	}

	/**
	 * Creates a stack the same way as the first constructor.
	 * This constructor is kept only so that programs already written
	 * with the static version of the stack keep working.
	 * @param size The wanted size of the stack.
	 */
	public LinkedStack(int size){
		this.top = null;
		this.count = 0;
	}

	/**
	 * Adds an element at the top of the stack.
	 *
	 * @param element the element to push.
	 * @return true if the operation succeeds and false otherwise (which never happens)
	 */
	public boolean push(T element) {
		Node newNode = new Node(top,element);
		this.top = newNode;
		this.count++;
		return true;
	}

	/**
	 * Removes the element at the top of the stack.
	 *
	 * @return The element at the top of the stack if it exists, null otherwise.
	 */
	public T pop(){

		if (isEmpty())
			return null;

		T removed = this.top.data;
		this.top = this.top.next;
		this.count--;
		return removed;
	}

	/**
	 * Indicates whether the stack is empty.
	 *
	 * @return true if the stack is empty and false otherwise.
	 */
	public boolean isEmpty(){

		return count==0; //or: return this.top==null;
	}

	/**
	 * Empties the stack.
	 */
	public void clear(){
		while (!isEmpty())
			pop();
	}

	/**
	 * Looks at the element at the top without removing it.
	 *
	 * @return The element at the top if the stack is not empty, null otherwise.
	 */
	public T peek(){
		if (this.top==null) //or : if (this.count==0)
			return null;
		return this.top.data;
	}


	/**
	 * Returns the number of elements in the stack.
	 *
	 * @return The number of elements currently in the stack.
	 */
	public int size(){

		return count;
	}

	private class Node implements Serializable {
		public T data;
		public Node next;

		public Node(Node next, T data) {
			this.data = data;
			this.next = next;
		}
	}
}
