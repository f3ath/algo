/**
 * @see http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 * @author f3ath
 */
public class Percolation {

    private WeightedQuickUnionUF union;
    private int size;
    private boolean[][] open;

    /**
     * Create N-by-N grid, with all sites blocked
     * @param N 
     */
    public Percolation(int N) {
        if (N < 1) {
            throw new java.lang.IllegalArgumentException("Positive number expected");
        }
        this.size = N;
        this.union = new WeightedQuickUnionUF(this.size * this.size);
        this.open = new boolean[this.size][this.size];
    }

    public static void main(String[] args) {
    }
    
    /**
     * Open site (row i, column j) if it is not open already
     * @param i
     * @param j
     */
    public void open(int i, int j) {
        i = this.convertIndex(i);
        j = this.convertIndex(j);

        this.open[i][j] = true;

        if (i > 0 && this.open[i - 1][j]) {
            this.union.union(this.getUnionIndex(i, j), this.getUnionIndex(i - 1, j));
        }
        if (j > 0 && this.open[i][j - 1]) {
            this.union.union(this.getUnionIndex(i, j), this.getUnionIndex(i, j - 1));
        }
        if (i < this.size - 1 && this.open[i + 1][j]) {
            this.union.union(this.getUnionIndex(i, j), this.getUnionIndex(i + 1, j));
        }
        if (j < this.size - 1 && this.open[i][j + 1]) {
            this.union.union(this.getUnionIndex(i, j), this.getUnionIndex(i, j + 1));
        }
    }

    /**
     * @param i
     * @param j
     * @return Is site (row i, column j) open?
     */
    public boolean isOpen(int i, int j) {
        i = this.convertIndex(i);
        j = this.convertIndex(j);
        return this.open[i][j];
    }

    /**
     * @param i
     * @param j
     * @return Is site (row i, column j) full?
     */
    public boolean isFull(int i, int j) {
        i = this.convertIndex(i);
        j = this.convertIndex(j);
        
        if (!this.open[i][j]) {
            return false;
        }

        for (int k = 0; k < this.size; k++) {
            if (this.union.connected(this.getUnionIndex(i, j), this.getUnionIndex(0, k))) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return Does percolate?
     */
    public boolean percolates() {
        for (int k = 1; k <= this.size; k++) {
            if (this.isFull(this.size, k)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Convert 1-based to 0-based
     * and check correctness
     *
     * @param i
     * @return Converted index
     */
    private int convertIndex(int i) {
        i--;
        if (i < 0 || i >= this.size) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return i;
    }

    /**
     * Convert 2-dimensional index into 1-dimensional
     * @param i
     * @param j
     * @return 1-dim index
     */
    private int getUnionIndex(int i, int j) {
        return this.size * i + j;
    }
}
