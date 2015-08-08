/**
 * @see http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 * @author f3ath
 */
public class PercolationStats {

    private int size;
    private int times;
    private Percolation percolation;
    private int[] rand;
    private double mean = 0;
    private double stddev = 0;

    /**
     * Perform T independent experiments on an N-by-N grid
     * @param N
     * @param T 
     */
    public PercolationStats(int N, int T) {
        if (N < 1) {
            throw new java.lang.IllegalArgumentException("Invalid grid size");
        }
        this.size = N;
        if (T < 2) {
            throw new java.lang.IllegalArgumentException("Invalid number of experiments");
        }
        this.times = T;
        this.rand = new int[N * N];

        for (int i = 0; i < this.rand.length; i++) {
            this.rand[i] = i;
        }

        double[] results = new double[T];
        double resultsSum = 0;

        for (int i = 0; i < T; i++) {
            results[i] = this.getExperimentResult();
            resultsSum += results[i];
        }

        this.mean = resultsSum / T;

        this.stddev = 0;
        for (int i = 0; i < T; i++) {
            double x = this.mean - results[i];
            this.stddev += (x * x);
        }
        this.stddev = Math.sqrt(this.stddev / (T - 1));
    }

    /**
     * @return Sample mean of percolation threshold
     */
    public double mean() {
        return this.mean;
    }

    /**
     * @return Sample standard deviation of percolation threshold
     */
    public double stddev() {
        return this.stddev;
    }

    /**
     * @return low  endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        return this.mean() - 1.96 * this.stddev() / Math.sqrt(this.times);
    }

    /**
     * @return high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return this.mean() + 1.96 * this.stddev() / Math.sqrt(this.times);
    }

    /**
     * test client
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
        this.percolation = new Percolation(this.size);
        this.shuffleRand(this.rand);
        int step = 0;
        while (!this.percolation.percolates()) {
            int index = this.rand[step++];
            this.percolation.open(index / this.size + 1, index % this.size + 1);
        }
        return step / (double) (this.size * this.size);
    }
}
