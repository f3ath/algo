
import java.util.Arrays;
import java.util.Iterator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author f3ath
 * @param <Item>
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    class RandomizedQueueIterator implements Iterator<Item> {

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
            if (current < items.length) {
                current++;
            }
            return items[current - 1];
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
            if (!result) {
                fail();
            }
            count++;
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

    private final boolean OCCUPIED = true;
    private final boolean EMPTY = false;

    /**
     * construct an empty randomized strQueue
     */
    public RandomizedQueue() {
        storage = (Item[]) new Object[4];
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
        if (size > (double) storage.length * 3 / 4) {
            storage = Arrays.copyOf(storage, storage.length * 2);
        }
    }

    /**
     * remove and return a random item
     *
     * @return
     */
    public Item dequeue() {
        int occupiedSlot = findRandomIndex(OCCUPIED);
        Item result = storage[occupiedSlot];
        storage[occupiedSlot] = null;
        size--;
        if (size < (double) storage.length / 4) {
            storage = getCompressedStorage(storage.length / 2);
        }
        return result;
    }

    /**
     * return (but do not remove) a random item
     *
     * @return
     */
    public Item sample() {
        return storage[findRandomIndex(OCCUPIED)];
    }

    /**
     * return an independent iterator over items in random order
     *
     * @return
     */
    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator(getCompressedStorage(size));
    }

    private int findRandomIndex(boolean occupied) {
        int index;
        do {
            index = StdRandom.uniform(storage.length);
        } while ((storage[index] == null) == occupied);
        return index;
    }

    /**
     * Get compressed copy of the storage
     *
     * @param newSize
     * @return
     */
    private Item[] getCompressedStorage(int newSize) {
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
