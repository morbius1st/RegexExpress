package pro.cyberstudio.regexexpress;

import org.jetbrains.annotations.NotNull;

import java.util.*;

import static pro.cyberstudio.regexexpress.Utility.*;

/**
 * @author jeffs
 *         File:    CircularList
 *         Created: 5/14/2017 @ 11:12 PM
 *         Project: RegexExpress
 */

class CircularList<T> implements Iterable<T> {
	
	private ArrayList<T> list;
	private int head = 0;
	private int size;
	
	CircularList(int size) {
		list = new ArrayList<>(size);
		this.size = size;
		for (int i = 0; i < size; i++) {
			list.add(null);
		}
		
	}
	
	void add(T element) {
		if (head + 1 > size) {
			head = 0;
		}
		list.add(head++, element);
	}

	
	@NotNull
	@Override
	public Iterator<T> iterator() {
		return new CircListIterator();
	}
	
	private class CircListIterator implements Iterator<T> {
		private int cursor;
		private int count;
		
		public CircListIterator() {
			int tail = head + 1;
			if (tail >= size)
				tail = 0;
			
			this.cursor = tail;
		}
		
		public boolean hasNext() {
			count++;
			return count <= size;
		}
		
		public T next() {
			if (cursor >= size) {
				cursor = 0;
			}
			
			LogMsgln("cursor " + cursor);
			return list.get(cursor++);
		}
		
		
	}
}
