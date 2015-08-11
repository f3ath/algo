
import java.io.File;

public class Brute {

    protected Point[] points;

    /**
     * Get points from a file
     *
     * @param file file name
     */
    public Brute(String file) {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        In in = new In(new File(file));
        points = new Point[in.readInt()];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(in.readInt(), in.readInt());
            points[i].draw();
        }
    }

    public static void main(String[] args) {
        Brute brute = new Brute(args[0]);
        brute.run();
    }

    public void run() {
        for (int a = 0; a < points.length - 3; a++) {
            for (int b = a + 1; b < points.length - 2; b++) {
                for (int c = b + 1; c < points.length - 1; c++) {
                    for (int d = c + 1; d < points.length; d++) {
                        if (collinear(points[a], points[b], points[c], points[d])) {
                            Point[] line = {points[a], points[b], points[c], points[d]};
                            Quick.sort(line);
                            outputLine(line);
                        }
                    }
                }
            }
        }
    }

    protected void outputLine(Point[] line) {
        System.out.print(line[0]);
        for (int i = 1; i < line.length; i++) {
            System.out.print(" -> " + line[i]);
        }
        System.out.println("");
        line[0].drawTo(line[line.length - 1]);
    }

    private boolean collinear(Point a, Point b, Point c, Point d) {
        return a.slopeTo(b) == a.slopeTo(c) && a.slopeTo(b) == a.slopeTo(d);
    }
}
