
/**
 * @see http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 * @author f3ath
 */
public class Percolation {

    private WeightedQuickUnionUF union;
    private int size;
    private int top;
    private int bottom;
    private boolean[][] open;

    /**
     * Create size-by-size grid, with all sites blocked
     *
     * @param size
     */
    public Percolation(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Positive number expected");
        }
        this.size = size;
        top = size * size;
        bottom = top + 1;
        union = new WeightedQuickUnionUF(size * size + 2);
        open = new boolean[size][size];
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
    }

    /**
     * Open site (row row, column col) if it is not open already
     *
     * @param row
     * @param col
     */
    public void open(int row, int col) {
        row = convertIndex(row);
        col = convertIndex(col);

        if (open[row][col]) {
            return;
        }

        open[row][col] = true;

        if (row == 0) {
            union.union(top, getUnionIndex(row, col));
        }
        if (row == size - 1) {
            union.union(bottom, getUnionIndex(row, col));
        }

        if (row > 0 && open[row - 1][col]) {
            union.union(getUnionIndex(row, col), getUnionIndex(row - 1, col));
        }
        if (col > 0 && open[row][col - 1]) {
            union.union(getUnionIndex(row, col), getUnionIndex(row, col - 1));
        }
        if (row < size - 1 && open[row + 1][col]) {
            union.union(getUnionIndex(row, col), getUnionIndex(row + 1, col));
        }
        if (col < size - 1 && open[row][col + 1]) {
            union.union(getUnionIndex(row, col), getUnionIndex(row, col + 1));
        }
    }

    /**
     * @param row
     * @param col
     * @return Is site (row row, column col) open?
     */
    public boolean isOpen(int row, int col) {
        row = convertIndex(row);
        col = convertIndex(col);
        return open[row][col];
    }

    /**
     * @param row
     * @param col
     * @return Is site (row row, column col) full?
     */
    public boolean isFull(int row, int col) {
        row = convertIndex(row);
        col = convertIndex(col);

        if (!open[row][col]) {
            return false;
        }
        return union.connected(top, getUnionIndex(row, col));
    }

    /**
     * @return Does percolate?
     */
    public boolean percolates() {
        return union.connected(top, bottom);
    }

    /**
     * Convert 1-based to 0-based and check correctness
     *
     * @param index
     * @return Converted index
     */
    private int convertIndex(int index) {
        index--;
        if (index < 0 || index >= size) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return index;
    }

    /**
     * Convert 2-dimensional index into 1-dimensional
     *
     * @param row
     * @param col
     * @return 1-dim index
     */
    private int getUnionIndex(int row, int col) {
        return size * row + col;
    }
}
