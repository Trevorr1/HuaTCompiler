package token;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class DoubleLinkedList<E> implements Iterable<E> {
	
	private int N;        // number of elements on list
    private Node pre;     // sentinel before first element
    private Node post;    // sentinel after last element
    
    private class Node {
        private E element;
        private Node next;
        private Node prev;
    }
	
	 public boolean isEmpty() { return N == 0; }
	 public int size() { return N; }
	 
	// add the element to the list
	public void add(E element) {
		Node last = post.prev;
		Node x = new Node();
		x.element = element;
		x.next = post;
		x.prev = last;
		post.prev = x;
		last.next = x;
		N++;
	}
	
	public ListIterator<E> iterator()  { return new DoublyLinkedListIterator(); }

    // assumes no calls to DoublyLinkedList.add() during iteration
    private class DoublyLinkedListIterator implements ListIterator<E> {
        private Node current = pre.next;  // the node that is returned by next()
        private Node lastAccessed = null; // the last node to be returned by prev() or next()
                                          // reset to null upon intervening remove() or add()
        private int index = 0;

        public boolean hasNext() { return index < N; }
        public boolean hasPrevious() { return index > 0; }
        public int previousIndex() { return index - 1; }
        public int nextIndex() { return index; }

        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastAccessed = current;
            E element = current.element;
            current = current.next; 
            index++;
            return element;
        }

        public E previous() {
            if (!hasPrevious()) throw new NoSuchElementException();
            current = current.prev;
            index--;
            lastAccessed = current;
            return current.element;
        }

        // replace the element of the element that was last accessed by next() or previous()
        // condition: no calls to remove() or add() after last call to next() or previous()
        public void set(E element) {
            if (lastAccessed == null) throw new IllegalStateException();
            lastAccessed.element = element;
        }

        // remove the element that was last accessed by next() or previous()
        // condition: no calls to remove() or add() after last call to next() or previous()
        public void remove() { 
            if (lastAccessed == null) throw new IllegalStateException();
            Node x = lastAccessed.prev;
            Node y = lastAccessed.next;
            x.next = y;
            y.prev = x;
            N--;
            if (current == lastAccessed)
                current = y;
            else
                index--;
            lastAccessed = null;
        }

        // add element to list 
        public void add(E element) {
            Node x = current.prev;
            Node y = new Node();
            Node z = current;
            y.element = element;
            x.next = y;
            y.next = z;
            z.prev = y;
            y.prev = x;
            N++;
            index++;
            lastAccessed = null;
        }

    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (E element : this)
            s.append(element + " ");
        return s.toString();
    }
}