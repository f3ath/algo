
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
     *
     * @param N
     */
    public Percolation(int N) {
        if (N < 1) {
            throw new java.lang.IllegalArgumentException("Positive number expected");
        }
        size = N;
        union = new WeightedQuickUnionUF(size * size);
        open = new boolean[size][size];
    }

    public static void main(String[] args) {
    }

    /**
     * Open site (row i, column j) if it is not open already
     *
     * @param i
     * @param j
     */
    public void open(int i, int j) {
        i = convertIndex(i);
        j = convertIndex(j);

        if (open[i][j]) {
            return;
        }

        open[i][j] = true;

        if (i > 0 && open[i - 1][j]) {
            union.union(getUnionIndex(i, j), getUnionIndex(i - 1, j));
        }
        if (j > 0 && open[i][j - 1]) {
            union.union(getUnionIndex(i, j), getUnionIndex(i, j - 1));
        }
        if (i < size - 1 && open[i + 1][j]) {
            union.union(getUnionIndex(i, j), getUnionIndex(i + 1, j));
        }
        if (j < size - 1 && open[i][j + 1]) {
            union.union(getUnionIndex(i, j), getUnionIndex(i, j + 1));
        }
    }

    /**
     * @param i
     * @param j
     * @return Is site (row i, column j) open?
     */
    public boolean isOpen(int i, int j) {
        i = convertIndex(i);
        j = convertIndex(j);
        return open[i][j];
    }

    /**
     * @param i
     * @param j
     * @return Is site (row i, column j) full?
     */
    public boolean isFull(int i, int j) {
        i = convertIndex(i);
        j = convertIndex(j);

        if (!open[i][j]) {
            return false;
        }

        int id = union.find(getUnionIndex(i, j));

        for (int k = 0; k < size; k++) {
            if (open[0][k] && id == union.find(getUnionIndex(0, k))) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return Does percolate?
     */
    public boolean percolates() {
        for (int k = 1; k <= size; k++) {
            if (isFull(size, k)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Convert 1-based to 0-based and check correctness
     *
     * @param i
     * @return Converted index
     */
    private int convertIndex(int i) {
        i--;
        if (i < 0 || i >= size) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return i;
    }

    /**
     * Convert 2-dimensional index into 1-dimensional
     *
     * @param i
     * @param j
     * @return 1-dim index
     */
    private int getUnionIndex(int i, int j) {
        return size * i + j;
    }
}
