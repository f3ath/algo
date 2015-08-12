
import java.io.File;

/**
 * Brute force line detection
 * @author f3ath
 */
public class Brute {
    
    private final Point[] points;

    /**
     * @param file file name
     */
    private Brute(String file) {
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
        Brute brute = new Brute(args[0]);
        brute.run();
    }

    /**
     * Run brute force comparison
     */
    private void run() {
        for (int a = 0; a < points.length - 3; a++) {
            for (int b = a + 1; b < points.length - 2; b++) {
                for (int c = b + 1; c < points.length - 1; c++) {
                    for (int d = c + 1; d < points.length; d++) {
                        Point[] line = {points[a], points[b], points[c], points[d]};
                        if (areCollinear(line)) {
                            Quick.sort(line);
                            outputLine(line);
                        }
                    }
                }
            }
        }
    }

    /**
     * Draw a line and output it to stdout
     *
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
    
    /**
     * Are the points collinear
     * @param points
     * @return 
     */
    private boolean areCollinear(Point[] points) {
        double slope = points[0].slopeTo(points[1]);
        for (int i = 2; i < points.length; i++) {
            if (slope != points[0].slopeTo(points[i])) {
                return false;
            }
        }
        return true;
    }
}
