package pro.cyberstudio.regexexpress;

import java.util.*;
import static pro.cyberstudio.regexexpress.Utility.*;

/**
 * @author jeffs
 *         File:    CircularList
 *         Created: 5/14/2017 @ 11:12 PM
 *         Project: RegexExpress
 */

class CircularStack<T> implements Iterable<T>{
	
	private ArrayList<T> stack;
	private int capacity;
	private int cursor = -1;
	private int size = 0;
	
	CircularStack(int capacity) {
		// stack is based on an ArrayList
		stack = new ArrayList<>(capacity);
		this.capacity = capacity;
		
		// pre assign the whold list so that the
		// stack.set method will work correctly
		for (int i = 0; i < capacity; i++) {
			stack.add(i, null);
		}
		
	}
	
	boolean push(T element) {
		if (cursor + 1 < capacity) {
			cursor++;
		} else {
			cursor = 0;
		}
		
		if (size < capacity) {
			size++;
		}
		
		stack.set(cursor, element);
		
		return true;
	}
	
	T pop() {
		if (size > 0) {
			int index = cursor;
			
			if (--size == 0) {
				cursor = -1;
			} else {
				if (--cursor < 0) {
					cursor = capacity - 1;
				}
			}
			
			return stack.get(index);
		}
		return null;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	int size() {
		return size;
	}
	
	int capacity() {
		return capacity;
	}
	
	public Iterator<T> iterator() {
		return stack.iterator();
	}
	
	void clear() {
		cursor = -1;
		size = 0;
	}

}
