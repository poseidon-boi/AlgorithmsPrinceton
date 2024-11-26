import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] randque = (Item[]) new Object[1];
    private int size = 0;

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < size; i++)
            temp[i] = randque[i];
        randque = temp;
    }

    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null argument");
        if (size == randque.length)
            resize(2 * randque.length);
        randque[size++] = item;
    }

    private void exchange(int pos) {
        if ((pos + 1) == size)
            return;
        Item temp = randque[pos];
        randque[pos] = randque[size - 1];
        randque[size - 1] = temp;
    }

    public Item dequeue() {
        if (size == 0)
            throw new NoSuchElementException("Empty queue");
        int pos = (int) (Math.random() * size);
        exchange(pos);
        if (size <= randque.length / 4)
            resize(randque.length / 2);
        Item dequeued = randque[--size];
        randque[size] = null;
        return dequeued;
    }

    public Item sample() {
        if (size == 0)
            throw new NoSuchElementException("Empty queue");
        int pos = (int) (Math.random() * size);
        exchange(pos);
        return randque[size - 1];
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private int current = 0;
        private int[] order = new int[size];

        public ListIterator() {
            int i;
            for (i = 0; i < size; i++)
                order[i] = i;
            for (i = 0; i < size; i++) {  // A Durstenfeld shuffle
                int random = (int) (Math.random() * (i + 1));
                if (random == i)
                    continue;
                int temp = order[i];
                order[i] = order[random];
                order[random] = temp;
            }
            // StdRandom.shuffle(order);
        }

        public boolean hasNext() {
            return current < size;
        }

        public Item next() {
            if (current >= size)
                throw new NoSuchElementException("No more items");
            return randque[order[current++]];
        }

        public void remove() {
            throw new UnsupportedOperationException("Operation not supported");
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<String> randqueue = new RandomizedQueue<>();
        System.out.println(randqueue.isEmpty());
        randqueue.enqueue("A");
        randqueue.enqueue("B");
        randqueue.enqueue("C");
        System.out.println(randqueue.sample());
        System.out.println(randqueue.size());
        for (String letter : randqueue)
            System.out.print(letter + " ");
        System.out.println();
        System.out.println(randqueue.dequeue());
        System.out.println(randqueue.dequeue());
        System.out.println(randqueue.dequeue());
    }
}
