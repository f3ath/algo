import java.io.File;
import java.util.Arrays;

/**
 * Fast line detection
 * @author f3ath
 */
public class Fast {

    private final Point[] points;

    /**
     * @param file 
     */
    private Fast(String file) {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        // get points from the file
        In in = new In(new File(file));
        points = new Point[in.readInt()];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(in.readInt(), in.readInt());
            points[i].draw();
        }
    }

    /**
     * @param args 
     */
    public static void main(String[] args) {
        Fast fast = new Fast(args[0]);
        fast.run();
    }

    private void run() {
        for (Point point : points) {
            findLinesThrough(point);
        }
    }

    /**
     * Find all lines going though the point
     * @param point 
     */
    private void findLinesThrough(Point point) {
        Point[] pointsBySlope = points.clone();
        Arrays.sort(pointsBySlope, point.SLOPE_ORDER);
        int lineStart = 0;
        int nextLineStart = 0;
        while (lineStart < pointsBySlope.length) {
            do {
                nextLineStart++;
            } while (nextLineStart < pointsBySlope.length
                    && point.SLOPE_ORDER.compare(pointsBySlope[lineStart], pointsBySlope[nextLineStart]) == 0);

            // now lineStart to nextLineStart-1 are colliner points
            if (nextLineStart - lineStart >= 3) { // there are 3 other points on the same line
                Point[] line = new Point[nextLineStart - lineStart + 1];
                line[0] = point; // the first point is the current one
                for (int i = lineStart; i < nextLineStart; i++) { // copy the rest of the line
                    line[i - lineStart + 1] = pointsBySlope[i];
                }
                Arrays.sort(line); // order point in the line
                if (point == line[0]) { // only consider lines starting from this point
                    outputLine(line);
                }
            }
            lineStart = nextLineStart; // moving to the next line
        }
    }
    
    /**
     * Draw the line, print to stdout
     * @param line 
     */
    private void outputLine(Point[] line) {
        System.out.print(line[0]);
        for (int i = 1; i < line.length; i++) {
            System.out.print(" -> " + line[i]);
        }
        System.out.println("");
        line[0].drawTo(line[line.length - 1]);
    }
}
