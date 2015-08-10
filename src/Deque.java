
import java.util.NoSuchElementException;
import java.util.Iterator;

/**
 * @author f3ath
 * @param <Item>
 */
public class Deque<Item> implements Iterable<Item> {

    private class Node {

        public Item item;
        public Node next;
        public Node prev;

        public Node(Item item, Node prev, Node next) {
            if (item == null) {
                throw new NullPointerException();
            }
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    private class DequeIterator implements Iterator<Item> {

        private Node node;

        public DequeIterator(Node node) {
            this.node = node;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Item next() {
            if (node == null) {
                throw new NoSuchElementException();
            }
            Item item = node.item;
            node = node.next;
            return item;
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }
    }

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

    private Node first;
    private Node last;
    private int size;

    /**
     * construct an deque deque
     */
    public Deque() {
        size = 0;
        first = last = null;
    }

    /**
     *
     * @return is the deque deque?
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * @return the number of items on the deque
     */
    public int size() {
        return size;
    }

    /**
     * add the item to the front
     *
     * @param item
     */
    public void addFirst(Item item) {
        if (isEmpty()) {
            init(item);
        } else {
            first = first.prev = new Node(item, null, first);
        }
        size++;
    }

    /**
     * add the item to the end
     *
     * @param item
     */
    public void addLast(Item item) {
        if (isEmpty()) {
            init(item);
        } else {
            last = last.next = new Node(item, last, null);
        }
        size++;
    }

    /**
     * remove and return the item from the front
     *
     * @return
     */
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node result = first;
        if (size == 1) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.prev = null;
        }
        result.next = null;
        size--;
        return result.item;
    }

    /**
     * remove and return the item from the end
     *
     * @return
     */
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node result = last;
        if (size == 1) {
            first = last = null;
        } else {
            last = last.prev;
            last.next = null;
        }
        result.prev = null;
        size--;
        return result.item;
    }

    /**
     * @return iterator over items in order from first to last
     */
    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator(first);
    }

    /**
     * init deque
     *
     * @param item
     */
    private void init(Item item) {
        last = first = new Node(item, null, null);
    }

    /**
     * unit testing
     *
     * @param args
     */
    public static void main(String[] args) {
        Test test = new Test();
        Deque<String> deque = new Deque<>();
        String str = "";

        test.check(deque.isEmpty());
        test.check(0 == deque.size());
        for (String s : deque) {
            str = str.concat(s);
        }
        test.check(str.equals(""));

        deque.addFirst("a");
        deque.addFirst("b");
        deque.addFirst("c");
        test.check(3 == deque.size());

        deque.addLast("x");
        deque.addLast("y");
        deque.addLast("z");
        test.check(6 == deque.size());

        str = "";
        for (String s : deque) {
            str = str.concat(s);
        }
        test.check(str.equals("cbaxyz"));

        test.check(deque.removeFirst().equals("c"));
        test.check(deque.removeFirst().equals("b"));
        test.check(deque.removeLast().equals("z"));
        test.check(deque.removeLast().equals("y"));
        test.check(2 == deque.size());
        test.check(!deque.isEmpty());

        test.check(deque.removeLast().equals("x"));
        test.check(deque.removeLast().equals("a"));
        test.check(deque.isEmpty());

        try {
            deque.removeFirst();
            test.fail();
        } catch (NoSuchElementException e) {
            test.check(true);
        }
        try {
            deque.removeLast();
            test.fail();
        } catch (NoSuchElementException e) {
            test.check(true);
        }

        System.out.println(test);
    }
}
