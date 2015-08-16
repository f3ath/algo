
import java.util.Iterator;

public class KdTree {

    private static class Node2D implements Comparable<Node2D> {

        private final Point2D point;
        private int type = 0;
        private Node2D left, right;

        private int getChildType() {
            return (type + 1) % 2;
        }

        public Node2D(Point2D point) {
            this.point = point;
        }

        @Override
        public int compareTo(Node2D that) {
            if (type % 2 == 0) {
                return Point2D.X_ORDER.compare(this.point, that.point);
            } else {
                return Point2D.Y_ORDER.compare(this.point, that.point);
            }
        }

        public boolean add(Node2D node) {
            return search(node, true) != null;
        }

        public boolean contains(Node2D node) {
            return search(node, false) != null;
        }

        public void draw() {
            if (left != null) {
                left.draw();
            }
            point.draw();
            if (right != null) {
                right.draw();
            }
        }

        public Node2D nearestTo(Node2D node, Node2D nearest) {
            // if current node is closer, update nearest
            if (node.point.distanceSquaredTo(point) < node.point.distanceSquaredTo(nearest.point)) {
                nearest = this;
            }
            Node2D first, second;
            if (compareTo(node) >= 0) { // order of search
                first = left;
                second = right;
            } else {
                first = right;
                second = left;
            }
            if (first != null) { // trying first half
                nearest = first.nearestTo(node, nearest);
            }
            // if second half is not empty and is possibly closer then nearest
            if (second != null
                    && borderDistanceSquaredTo(node) < node.point.distanceSquaredTo(nearest.point)) {
                nearest = second.nearestTo(node, nearest);
            }

            return nearest;
        }

        /**
         * Squared distance from the border created by this node to the other
         *
         * @param point
         * @return
         */
        private double borderDistanceSquaredTo(Node2D node) {
            if (type % 2 == 0) { // X-distance
                return Math.pow(point.x() - node.point.x(), 2);
            } else { // Y-distance
                return Math.pow(point.y() - node.point.y(), 2);
            }
        }

        /**
         * Search for the node in the tree
         *
         * @param node
         * @param insert Insert if not pointsInRange
         * @return pointsInRange node or null
         */
        public Node2D search(Node2D node, boolean insert) {
            if (point.equals(node.point)) {
                return this;
            }
            if (compareTo(node) >= 0) {
                if (left != null) {
                    return left.search(node, insert);
                }
                if (insert) {
                    node.type = getChildType();
                    left = node;
                    return node;
                } else {
                    return null;
                }
            } else {
                if (right != null) {
                    return right.search(node, insert);
                }
                if (insert) {
                    node.type = getChildType();
                    right = node;
                    return node;
                } else {
                    return null;
                }
            }
        }

        private boolean hasOnTheLeft(RectHV rect) {
            if (type % 2 == 0) { // X-distance
                return point.x() >= rect.xmin();
            } else { // Y-distance
                return point.y() >= rect.ymin();
            }
        }

        private boolean hasOnTheRight(RectHV rect) {
            if (type % 2 == 0) { // X-distance
                return point.x() <= rect.xmax();
            } else { // Y-distance
                return point.y() <= rect.ymax();
            }
        }

        public void range(RectHV rect, Stack<Point2D> found) {
            if (rect.contains(point)) {
                found.push(point);
            }
            if (left != null && hasOnTheLeft(rect)) {
                left.range(rect, found);
            }
            if (right != null && hasOnTheRight(rect)) {
                right.range(rect, found);
            }
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

    private Node2D root;
    private int size;

    /**
     * construct an empty set of points
     */
    public KdTree() {
    }

    /**
     * @return is the set empty?
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * @return number of points in the set
     */
    public int size() {
        return size;
    }

    /**
     * search the point to the set (if it is not already in the set)
     *
     * @param point
     */
    public void insert(Point2D point) {
        if (root == null) {
            root = new Node2D(point);
            size++;
        } else if (root.add(new Node2D(point))) {
            size++;
        }
    }

    /**
     * @param p
     * @return does the set contain point p?
     */
    public boolean contains(Point2D p) {
        if (isEmpty()) {
            return false;
        } else {
            return root.search(new Node2D(p), false) != null;
        }
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        if (root != null) {
            root.draw();
        }
    }

    /**
     *
     * @param rect
     * @return all points that are inside the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        Stack<Point2D> pointsInRange = new Stack<>();
        if (root != null) {
            root.range(rect, pointsInRange);
        }
        return pointsInRange;
    }

    /**
     *
     * @param p
     * @return a nearest neighbor in the set to point p; null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        if (root == null) {
            return null;
        } else {
            return root.nearestTo(new Node2D(p), root).point;
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Test test = new Test("PointSET");

        KdTree set = new KdTree();
        test.that(null == set.nearest(new Point2D(0, 0)));
        test.that(0 == set.size());
        set.insert(new Point2D(2, 1));
        set.insert(new Point2D(2, 1)); // same element
        test.that(1 == set.size());
        set.insert(new Point2D(1, 3));
        test.that(2 == set.size());

        test.that(set.nearest(new Point2D(2, 4)).equals(new Point2D(1, 3)));

        Iterator<Point2D> pointsInRange = set.range(new RectHV(0, 2, 2, 4)).iterator();
        test.that(pointsInRange.next().equals(new Point2D(1, 3)));
        test.that(!pointsInRange.hasNext());
        System.out.println(test);
    }

}
