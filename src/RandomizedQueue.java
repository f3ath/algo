
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 * @author f3ath
 * @param <Item>
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private class RandomizedQueueIterator implements Iterator<Item> {

        private final Item[] items;
        private int current;

        public RandomizedQueueIterator(Item[] payload) {
            items = payload;
            StdRandom.shuffle(items);
            current = 0;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Item next() {
            if (current >= items.length) {
                throw new NoSuchElementException();
            } else {
                return items[current++];
            }
        }

        @Override
        public boolean hasNext() {
            return current < items.length;
        }
    }

    /**
     *
     */
    private static class Test {

        private int count = 0;

        public void check(boolean result) {
            if (result) {
                count++;
            } else {
                fail();
            }
        }

        public void fail() {
            throw new RuntimeException("Test failed");
        }

        @Override
        public String toString() {
            return String.format("Tests run: %d", count);
        }
    }

    private Item[] storage;

    private int size = 0;

    private static final boolean OCCUPIED = true;
    private static final boolean EMPTY = false;

    private static final double MAX_CAPACITY_RATIO = (double) 3 / 4;
    private static final double MIN_CAPACITY_RATIO = (double) 1 / 4;
    private static final int RESIZE_FACTOR = 2;
    private static final int INITIAL_CAPACITY = 4;

    /**
     * construct an empty randomized strQueue
     */
    public RandomizedQueue() {
        storage = (Item[]) new Object[INITIAL_CAPACITY];
    }

    /**
     *
     * @return is the strQueue empty?
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     *
     * @return the number of items on the strQueue
     */
    public int size() {
        return size;
    }

    /**
     * add the item
     *
     * @param item
     */
    public void enqueue(Item item) {
        storage[findRandomIndex(EMPTY)] = item;
        size++;
        if (size > storage.length * MAX_CAPACITY_RATIO) {
            storage = getStorageCopy(storage.length * RESIZE_FACTOR);
        }
    }

    /**
     * remove and return a random item
     *
     * @return
     */
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int occupiedSlot = findRandomIndex(OCCUPIED);
        Item result = storage[occupiedSlot];
        storage[occupiedSlot] = null;
        size--;
        if (size < storage.length * MIN_CAPACITY_RATIO) {
            storage = getStorageCopy(storage.length / RESIZE_FACTOR);
        }
        return result;
    }

    /**
     * return (but do not remove) a random item
     *
     * @return
     */
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return storage[findRandomIndex(OCCUPIED)];
    }

    /**
     * return an independent iterator over items in random order
     *
     * @return
     */
    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator(getStorageCopy(size));
    }

    /**
     *
     * @param occupied
     * @return
     */
    private int findRandomIndex(boolean occupied) {
        int index;
        do {
            index = StdRandom.uniform(storage.length);
        } while ((storage[index] == null) == occupied);
        return index;
    }

    /**
     * Get a (de)compressed copy of the storage
     *
     * @param newSize
     * @return
     */
    private Item[] getStorageCopy(int newSize) {
        Item[] newStorage = (Item[]) new Object[newSize];
        int newIndex = 0;
        for (Item item : storage) {
            if (item != null) {
                newStorage[newIndex++] = item;
            }
        }
        return newStorage;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Test test = new Test();

        RandomizedQueue<String> strQueue = new RandomizedQueue<>();
        test.check(strQueue.isEmpty());
        test.check(0 == strQueue.size());

        try {
            strQueue.dequeue();
            test.fail();
        } catch (NoSuchElementException e) {
            test.check(true);
        }
        try {
            strQueue.sample();
            test.fail();
        } catch (NoSuchElementException e) {
            test.check(true);
        }

        strQueue.enqueue("a");
        test.check(!strQueue.isEmpty());
        test.check(1 == strQueue.size());
        test.check(strQueue.sample().equals("a"));
        test.check(!strQueue.isEmpty());
        test.check(1 == strQueue.size());
        test.check(strQueue.dequeue().equals("a"));
        test.check(strQueue.isEmpty());
        test.check(0 == strQueue.size());

        RandomizedQueue<Integer> intQueue = new RandomizedQueue<>();

        int size = 1000000;
        for (int i = 0; i < size; i++) {
            intQueue.enqueue(i);
            test.check(i + 1 == intQueue.size());
        }
        int[] counts = new int[size];
        for (int i = 0; i < size; i++) {
            counts[intQueue.dequeue()]++;
            test.check(size - i - 1 == intQueue.size());

        }
        for (int i = 0; i < size; i++) {
            test.check(1 == counts[i]);
        }

        System.out.println(test);
    }
}
