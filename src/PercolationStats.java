
/**
 * @see http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 * @author f3ath
 */

public class PercolationStats {

    private int size;
    private int times;
    private int[] rand;
    private double mean = 0;
    private double stddev = 0;

    /**
     * Perform T independent experiments on an N-by-N grid
     *
     * @param N
     * @param T
     */
    public PercolationStats(int N, int T) {
        if (N < 1) {
            throw new IllegalArgumentException("Invalid grid size");
        }
        if (T < 1) {
            throw new IllegalArgumentException("Invalid number of experiments");
        }
        size = N;
        times = T;
        rand = new int[N * N];

        for (int i = 0; i < rand.length; i++) {
            rand[i] = i;
        }

        double[] results = new double[T];
        double resultsSum = 0;

        for (int i = 0; i < T; i++) {
            results[i] = getExperimentResult();
            resultsSum += results[i];
        }

        mean = resultsSum / T;

        stddev = 0;
        for (int i = 0; i < T; i++) {
            stddev += Math.pow(mean - results[i], 2);
        }
        stddev = Math.sqrt(stddev / (T - 1));
    }

    /**
     * @return Sample mean of percolation threshold
     */
    public double mean() {
        return mean;
    }

    /**
     * @return Sample standard deviation of percolation threshold
     */
    public double stddev() {
        return stddev;
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
     * Shuffle the array
     *
     * @param a
     */
    private void shuffleRand(int[] a) {
        for (int i = a.length - 1; i > 0; i--) {
            int index = StdRandom.uniform(i);
            int swp = a[i];
            a[i] = a[index];
            a[index] = swp;
        }
    }

    private double getExperimentResult() {
        Percolation p = new Percolation(size);
        shuffleRand(rand);
        int step = 0;
        while (!p.percolates()) {
            int index = rand[step++];
            p.open(index / size + 1, index % size + 1);
        }
        return step / (double) (size * size);
    }
}
