
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * @see http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 * @author f3ath
 */
public class Percolation {

    private final WeightedQuickUnionUF union;
    private final int size;
    private final int top;
    private final boolean[][] open;

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
        union = new WeightedQuickUnionUF(size * size + 2);
        open = new boolean[size][size];
    }

    /**
     * Required by API
     *
     * @param args
     */
    public static void main(String[] args) {
    }

    /**
     * Open site if it is not open already
     *
     * @param r row
     * @param c column
     */
    public void open(int r, int c) {
        final int row = convertIndex(r);
        final int col = convertIndex(c);

        if (open[row][col]) {
            return;
        }

        open[row][col] = true;

        final int cell = getUnionIndex(row, col);

        if (row == 0) {
            union.union(cell, top);
        }
        if (row > 0 && open[row - 1][col]) { // up
            union.union(cell, getUnionIndex(row - 1, col));
        }
        if (col > 0 && open[row][col - 1]) { // left
            union.union(cell, getUnionIndex(row, col - 1));
        }
        if (row < size - 1 && open[row + 1][col]) { // down
            union.union(cell, getUnionIndex(row + 1, col));
        }
        if (col < size - 1 && open[row][col + 1]) { // right
            union.union(cell, getUnionIndex(row, col + 1));
        }
    }

    /**
     * @param r row
     * @param c col
     * @return Is site open?
     */
    public boolean isOpen(int r, int c) {
        return open[convertIndex(r)][convertIndex(c)];
    }

    /**
     * @param r row
     * @param c col
     * @return Is site full?
     */
    public boolean isFull(int r, int c) {
        final int row = convertIndex(r);
        final int col = convertIndex(c);

        if (!open[row][col]) {
            return false;
        }
        return union.connected(top, getUnionIndex(row, col));
    }

    /**
     * @return Does percolate?
     */
    public boolean percolates() {
        int topId = union.find(top);
        int bottomRow = size - 1;
        int offset = size * bottomRow;
        for (int col = 0; col < size; col++) {
            if (open[bottomRow][col] && union.find(offset + col) == topId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Convert 1-based to 0-based and check correctness
     *
     * @param index
     * @return Converted index
     */
    private int convertIndex(int index) {
        if (index <= 0 || index > size) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return index - 1;
    }

    /**
     * Convert 2-dimensional index into 1-dimensional
     *
     * @param r row
     * @param c col
     * @return 1-dim index
     */
    private int getUnionIndex(int r, int c) {
        return size * r + c;
    }
}
