
import java.util.Arrays;

public class Fast extends Brute {

    public Fast(String file) {
        super(file);
    }

    public static void main(String[] args) {
        Fast fast = new Fast(args[0]);
        fast.run();
    }

    @Override
    public void run() {
        for (Point p : points) {
        }
    }

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
}
