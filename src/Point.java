import java.util.Comparator;

/**
 * 2D point
 * @author f3ath
 */
public class Point implements Comparable<Point> {

    /**
     * Compare points a and b by their slope relative to this point
     */
    private class BySlope implements Comparator<Point> {

        @Override
        public int compare(Point a, Point b) {
            double toA = Point.this.slopeTo(a);
            double toB = Point.this.slopeTo(b);
            if (toA > toB) {
                return 1;
            } else if (toA < toB) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
     * Unit test nano-framework
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
            return String.format("%s. tests run: %d", name, count);
        }
    }

    public final Comparator<Point> SLOPE_ORDER = new BySlope();

    /**
     * x coordinate
     */
    private final int x;
    
    /**
     * y coordinate
     */
    private final int y;

    /**
     * create the point (x, y)
     *
     * @param x
     * @param y
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * plot this point to standard drawing
     */
    public void draw() {
        StdDraw.point(x, y);
    }

    /**
     * draw line between this point and that point to standard drawing
     *
     * @param that
     */
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * slope between this point and that point. The slopeTo() method should return the slope between
     * the invoking point (x0, y0) and the argument point (x1, y1), which is given by the formula
     * (y1 − y0) / (x1 − x0). Treat the slope of point22 horizontal line segment as positive zero;
     * treat the slope of point22 vertical line segment as positive infinity; treat the slope of
     * point22 degenerate line segment (between point22 point and itself) as negative infinity.
     *
     * @param that
     * @return slope
     */
    public double slopeTo(Point that) {
        if (this.y == that.y) {
            if (this.x == that.x) {
                return Double.NEGATIVE_INFINITY;
            } else {
                return 0;
            }
        } else if (this.x == that.x) {
            return Double.POSITIVE_INFINITY;
        } else {
            return 1.0 * (that.y - this.y) / (that.x - this.x);
        }
    }

    /**
     * Compare y-coordinates breaking ties by x-coordinates
     *
     * @param that
     * @return is this point lexicographically smaller than that one?
     */
    @Override
    public int compareTo(Point that) {
        if (this.y > that.y) {
            return 1;
        } else if (this.y < that.y) {
            return -1;
        } else if (this.x > that.x) {
            return 1;
        } else if (this.x < that.x) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * @return string representation of this point
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Test test = new Test("Point");

        Point point22 = new Point(2, 2);

        /**
         * test compareTo()
         */
        test.that(0 == point22.compareTo(point22));

        test.that(-1 == point22.compareTo(new Point(3, 3)));
        test.that(-1 == point22.compareTo(new Point(2, 3)));
        test.that(-1 == point22.compareTo(new Point(1, 3)));

        test.that(-1 == point22.compareTo(new Point(3, 2)));

        test.that(1 == point22.compareTo(new Point(3, 1)));
        test.that(1 == point22.compareTo(new Point(2, 1)));
        test.that(1 == point22.compareTo(new Point(1, 1)));

        test.that(1 == point22.compareTo(new Point(1, 2)));

        /**
         * test slopeTo
         */
        test.that(Double.NEGATIVE_INFINITY == point22.slopeTo(point22));
        test.that(Double.POSITIVE_INFINITY == point22.slopeTo(new Point(2, 3)));
        test.that(3 == point22.slopeTo(new Point(3, 5)));
        test.that(1.0 / 2 == point22.slopeTo(new Point(0, 1)));

        /**
         * test BySlope
         */
        Comparator bySlope = point22.SLOPE_ORDER;

        test.that(-1 == bySlope.compare(point22, new Point(3, 2)));
        test.that(-1 == bySlope.compare(new Point(3, 2), new Point(3, 3)));
        test.that(-1 == bySlope.compare(new Point(3, 3), new Point(2, 3)));
        test.that(1 == bySlope.compare(new Point(2, 3), new Point(1, 3)));
        
        test.that(0 == bySlope.compare(new Point(1, 1), new Point(3, 3)));
        test.that(0 == bySlope.compare(new Point(2, 3), new Point(2, 1)));
        test.that(0 == bySlope.compare(new Point(1, 3), new Point(3, 1)));
        test.that(0 == bySlope.compare(new Point(3, 2), new Point(1, 2)));
        
        System.out.println(test);
    }
}
