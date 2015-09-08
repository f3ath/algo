
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * @see http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 * @author f3ath
 */
public class PercolationStats {

    private final int size;
    private final int times;
    private final double[] results;

    /**
     * Perform experimentsCount independent experiments on an size-by-size grid
     *
     * @param size
     * @param experimentsCount
     */
    public PercolationStats(int size, int experimentsCount) {
        if (size < 1) {
            throw new IllegalArgumentException("Invalid grid size");
        }
        if (experimentsCount < 1) {
            throw new IllegalArgumentException("Invalid number of experiments");
        }
        this.size = size;
        times = experimentsCount;

        results = new double[experimentsCount];

        for (int i = 0; i < experimentsCount; i++) {
            results[i] = getExperimentResult();
        }
    }

    /**
     * @return Sample mean of percolation threshold
     */
    public double mean() {
        return StdStats.mean(results);
    }

    /**
     * @return Sample standard deviation of percolation threshold
     */
    public double stddev() {
        return StdStats.stddev(results);
    }

    /**
     * @return low endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(times);
    }

    /**
     * @return high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(times);
    }

    /**
     * test client
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Not enough args");
            return;
        }
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, t);

        System.out.format("mean                    = %f\n", stats.mean());
        System.out.format("stddev                  = %f\n", stats.stddev());
        System.out.format("95%% confidence interval = %f, %f\n", stats.confidenceLo(), stats.confidenceHi());
    }

    /**
     * Run experiment
     *
     * @return
     */
    private double getExperimentResult() {
        int[] rand = new int[size * size];
        for (int i = 0; i < rand.length; i++) {
            rand[i] = i;
        }
        StdRandom.shuffle(rand);
        Percolation percolation = new Percolation(size);
        int step = 0;
        while (!percolation.percolates()) {
            int index = rand[step++];
            percolation.open(index / size + 1, index % size + 1);
        }
        return step / (double) (size * size);
    }
}
