
import java.util.Comparator;
import java.util.TreeSet;
import java.util.Iterator;

/**
 * Brute force implementation
 *
 * @author f3ath
 */
public class PointSET{

    private static final class XYOrder implements Comparator<Point2D> {

        @Override
        public int compare(Point2D a, Point2D b) {
            int xOrder = Point2D.X_ORDER.compare(a, b);
            return xOrder != 0 ? xOrder : Point2D.Y_ORDER.compare(a, b);
        }
    }

    /**
     * Unit-test nano-framework
     */
    private static class Test {

        private final String name;
        private int count = 0;

        public Test(String name) {
            this.name = name;
        }

        public void that(boolean result) {
            if (result) {
                count++;
            } else {
                throw new RuntimeException("Test failed");
            }
        }

        public void pass() {
            that(true);
        }

        public void fail() {
            that(false);
        }

        @Override
        public String toString() {
            return String.format("%s tests passed: %d", name, count);
        }
    }

    private static final Comparator<Point2D> XY_ORDER = new XYOrder();
    private final TreeSet<Point2D> tree; 

    /**
     * construct an empty set of points
     */
    public PointSET() {
        tree = new TreeSet<>(XY_ORDER);
    }

    /**
     * add the point to the set (if it is not already in the set)
     *
     * @param p
     */
    public void insert(Point2D p) {
        if (!tree.contains(p)) {
            tree.add(p);
        }
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        for (Point2D p : tree) {
            p.draw();
        }
    }

    /**
     *
     * @param rect
     * @return all points that are inside the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        Stack<Point2D> range = new Stack<>();
        for (Point2D p : tree) {
            if (rect.contains(p)) {
                range.push(p);
            }
        }
        return range;
    }
    
    public int size() {
        return tree.size();
    }

    /**
     *
     * @param origin
     * @return a nearest neighbor in the set to point origin; null if the set is empty
     */
    public Point2D nearest(Point2D origin) {
        if (isEmpty()) {
            return null;
        }
        Point2D nearest = tree.first();
        double minDistanceSoFar = origin.distanceSquaredTo(nearest);
        for (Point2D p : tree) {
            double distance = origin.distanceSquaredTo(p);
            if (distance < minDistanceSoFar) {
                minDistanceSoFar = distance;
                nearest = p;
            }
        }
        return nearest;
    }
    
    public boolean isEmpty() {
        return tree.isEmpty();
    }
    
    public boolean contains(Point2D p) {
        return tree.contains(p);
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Test test = new Test("PointSET");
        test.that(0 == XY_ORDER.compare(new Point2D(2, 3), new Point2D(2, 3)));
        test.that(1 == XY_ORDER.compare(new Point2D(3, 3), new Point2D(2, 3)));
        test.that(-1 == XY_ORDER.compare(new Point2D(1, 3), new Point2D(2, 3)));
        test.that(1 == XY_ORDER.compare(new Point2D(2, 3), new Point2D(2, 2)));
        test.that(-1 == XY_ORDER.compare(new Point2D(2, 3), new Point2D(2, 4)));
        
        PointSET set = new PointSET();
        test.that(null == set.nearest(new Point2D(0, 0)));
        test.that(0 == set.size());
        set.insert(new Point2D(2, 1));
        set.insert(new Point2D(2, 1)); // same element
        test.that(1 == set.size());
        set.insert(new Point2D(1, 3));
        test.that(2 == set.size());
        test.that(set.nearest(new Point2D(0, 0)).equals(new Point2D(2, 1)));

        Iterator<Point2D> range = set.range(new RectHV(0, 2, 2, 4)).iterator();
        test.that(range.next().equals(new Point2D(1, 3)));
        test.that(!range.hasNext());
        
        System.out.println(test);
    }

}
