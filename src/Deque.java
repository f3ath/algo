
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

        public int c = 0;

        public void test(boolean b) {
            if (!b) {
                throw new RuntimeException("Test failed");
            }
            c++;
        }

        @Override
        public String toString() {
            return String.format("Tests run: %d", c);
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
            first = last = null;
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
     * unit testing
     *
     * @param args
     */
    public static void main(String[] args) {
        Test test = new Test();
        Deque<String> deque = new Deque<>();
        String str = "";

        test.test(deque.isEmpty());
        test.test(0 == deque.size());
        for (String s : deque) {
            str += s;
        }
        test.test(str.equals(""));

        deque.addFirst("a");
        deque.addFirst("b");
        deque.addFirst("c");
        test.test(3 == deque.size());

        deque.addLast("x");
        deque.addLast("y");
        deque.addLast("z");
        test.test(6 == deque.size());

        str = "";
        for (String s : deque) {
            str += s;
        }
        test.test(str.equals("cbaxyz"));

        test.test(deque.removeFirst().equals("c"));
        test.test(deque.removeFirst().equals("b"));
        test.test(deque.removeLast().equals("z"));
        test.test(deque.removeLast().equals("y"));
        test.test(2 == deque.size());
        test.test(!deque.isEmpty());

        test.test(deque.removeLast().equals("x"));
        test.test(deque.removeLast().equals("a"));
        test.test(deque.isEmpty());

        try {
            deque.removeFirst();
            test.test(false);
        } catch (NoSuchElementException e) {
            test.test(true);
        }
        try {
            deque.removeLast();
            test.test(false);
        } catch (NoSuchElementException e) {
            test.test(true);
        }

        System.out.println(test);
    }

    /**
     * init deque
     *
     * @param item
     */
    private void init(Item item) {
        last = first = new Node(item, null, null);
    }
}
