import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int size = 0;

    private class Node {
        Item item;
        Node next;
        Node previous;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null argument");
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        if (!isEmpty())
            oldfirst.previous = first;
        first.next = oldfirst;
        first.previous = null;
        if (isEmpty()) {
            first.next = null;
            last = first;
        }
        size++;
    }

    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null argument");
        Node oldlast = last;
        last = new Node();
        last.item = item;
        if (!isEmpty())
            oldlast.next = last;
        last.previous = oldlast;
        last.next = null;
        if (isEmpty()) {
            last.previous = null;
            first = last;
        }
        size++;
    }

    public Item removeFirst() {
        if (size == 0)
            throw new NoSuchElementException("Empty deque");
        Node oldfirst = first;
        Item item = oldfirst.item;
        if (size == 1) {
            first = null;
            last = null;
        }
        else {
            first = oldfirst.next;
            first.previous = null;
        }
        size--;
        return item;
    }

    public Item removeLast() {
        if (size == 0)
            throw new NoSuchElementException("Empty deque");
        Node oldlast = last;
        Item item = oldlast.item;
        if (size == 1) {
            last = null;
            first = null;
        }
        else {
            last = oldlast.previous;
            last.next = null;
        }
        size--;
        return item;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (current == null)
                throw new NoSuchElementException("No more items");
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("Operation not supported");
        }
    }

    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        System.out.println(deque.isEmpty());
        System.out.println(deque.size());
        deque.addFirst("A");
        deque.addLast("B");
        for (String var : deque)
            System.out.println(var);
        System.out.println(deque.size());
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeLast());
    }
}
