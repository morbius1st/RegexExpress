package pro.cyberstudio.regexexpress;

/**
 * @author jeffs
 *         File:    CircularList
 *         Created: 5/14/2017 @ 11:12 PM
 *         Project: RegexExpress
 */

class CircularStack<T> { //implements Iterable<T>{

//	private ArrayList<T> stack;
	private T[] stack;
	private int capacity;
	private int cursor = -1;
	private int size = 0;
	
	CircularStack(int capacity) {
		// stack is based on an ArrayList
//		stack = new ArrayList<>(capacity);
		stack = (T[]) new Object[capacity];
		this.capacity = capacity;
		
//		// pre assign the whole list so that the
//		// stack.set method will work correctly
//		for (int i = 0; i < capacity; i++) {
//			stack.add(i, null);
//		}
	
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
		
//		stack.set(cursor, element);
		stack[cursor] = element;
		
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
			
//			return stack.get(index);
			return stack[index];
		}
		
		return null;
	}
	
	T peek() {
		if (isEmpty())
			return null;
		
//		return stack.get(cursor);
		return stack[cursor];
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
	
//	public Iterator<T> iterator() {
//		return stack.iterator();
//	}
	
	void clear() {
		cursor = -1;
		size = 0;
	}

}
