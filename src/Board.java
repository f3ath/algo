
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Playing board
 *
 * @author f3ath
 */
public class Board {

    /**
     * Iterable neighbors
     */
    private class Neighbors implements Iterable<Board> {

        private int[][] blocks;

        /**
         * @param blocks
         */
        public Neighbors(int[][] blocks) {
            this.blocks = blocks;
        }

        public Iterator<Board> iterator() {
            return new NeighborsIterator(blocks);
        }
    }

    /**
     * Helper class
     */
    private static class ArrayCopy {

        /**
         * get a deep copy of int[][]
         *
         * @param blocks
         * @return
         */
        public static int[][] deep(int[][] blocks) {
            int[][] copy = new int[blocks.length][blocks[0].length];
            for (int i = 0; i < blocks.length; i++) {
                System.arraycopy(blocks[i], 0, copy[i], 0, blocks[i].length);
            }
            return copy;
        }
    }

    /**
     * Iterator through neighbors
     */
    private class NeighborsIterator implements Iterator<Board> {

        private int step = 0;
        private int iZero, jZero;
        private int[][] moves = new int[4][2];
        private int[][] blocks;
        private Board nextNeighbor;

        public NeighborsIterator(int[][] blocks) {
            this.blocks = blocks;
            outerloop:
            for (int i = 0; i < dimension(); i++) {
                for (int j = 0; j < dimension(); j++) {
                    if (blocks[i][j] == 0) {
                        iZero = i;
                        jZero = j;
                        break outerloop;
                    }
                }
            }
            // candidates to move
            moves[0][0] = iZero - 1;
            moves[0][1] = jZero;

            moves[1][0] = iZero + 1;
            moves[1][1] = jZero;

            moves[2][0] = iZero;
            moves[2][1] = jZero - 1;

            moves[3][0] = iZero;
            moves[3][1] = jZero + 1;

            nextNeighbor = buildNext();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * Get next possible step. There are not more then 4 possible steps: 0: upper tile down 1:
         * right tile left 2: down tile up 3: left tile right
         *
         * @return
         */
        @Override
        public Board next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Board result = nextNeighbor;
            nextNeighbor = buildNext();
            return result;
        }

        @Override
        public boolean hasNext() {
            return nextNeighbor != null;
        }

        /**
         * Build next possible neighbor
         */
        private Board buildNext() {
            if (step > 3) {
                return null;
            }

            // coord of the candidate to swap
            int i = moves[step][0];
            int j = moves[step][1];

            step++;

            if (i < 0 || i >= blocks.length || j < 0 || j >= blocks[0].length) {
                // out of bounds, skip this step
                return buildNext();
            }

            int[][] nextBlocks = ArrayCopy.deep(blocks);

            nextBlocks[iZero][jZero] = nextBlocks[i][j];
            nextBlocks[i][j] = 0;
            return new Board(nextBlocks);
        }
    }

    /**
     * Unit-test nano-framework
     */
    private static class Test {

        private final String name;
        private int count = 0;

        public Test(String name) {
            this.name = name;
        }

        public void that(boolean result) {
            if (result) {
                count++;
            } else {
                throw new RuntimeException("Test failed");
            }
        }

        public void pass() {
            that(true);
        }

        public void fail() {
            that(false);
        }

        @Override
        public String toString() {
            return String.format("%s tests passed: %d", name, count);
        }
    }
    private final int[][] blocks;
    private int hamming = -1;
    private int manhattan = -1;

    /**
     * construct a board2x2 from an N-by-N array of blocks2x2 where blocks2x2[i][j] = block in row
     * i, column j
     *
     * @param blocks
     */
    public Board(int[][] blocks) {
        if (blocks.length < 2) {
            throw new IllegalArgumentException("Board is too small");
        }
        if (blocks.length != blocks[0].length) {
            throw new IllegalArgumentException("Board is not square");
        }
        this.blocks = ArrayCopy.deep(blocks);
    }

    /**
     * board2x2 dimension N
     *
     * @return
     */
    public int dimension() {
        return blocks.length;
    }

    /**
     * number of blocks2x2 out of place
     *
     * @return
     */
    public int hamming() {
        if (hamming == -1) {
            hamming = 0;
            for (int i = 0; i < dimension(); i++) {
                for (int j = 0; j < dimension(); j++) {
                    if (blocks[i][j] != 0 && blocks[i][j] != getExpectedBlock(i, j)) {
                        hamming++;
                    }
                }
            }
        }
        return hamming;
    }

    /**
     * sum of Manhattan distances between blocks2x2 and goal
     *
     * @return
     */
    public int manhattan() {
        if (manhattan == -1) {
            manhattan = 0;
            for (int i = 0; i < dimension(); i++) {
                for (int j = 0; j < dimension(); j++) {
                    if (blocks[i][j] != 0) {
                        manhattan += Math.abs(i - (blocks[i][j] - 1) / dimension());
                        manhattan += Math.abs(j - (blocks[i][j] - 1) % dimension());
                    }
                }
            }
        }
        return manhattan;
    }

    /**
     * is this board2x2 the goal board2x2?
     *
     * @return
     */
    public boolean isGoal() {
        return 0 == hamming();
    }

    /**
     * a board2x2 that is obtained by exchanging two adjacent blocks2x2 in the same row
     *
     * @return
     */
    public Board twin() {
        int[][] twinBlocks = ArrayCopy.deep(blocks);
        int swap;
        int i1, i2, j1, j2;
        if (twinBlocks[0][0] == 0 || twinBlocks[0][1] == 0) {
            // zero is in the first row, exchange in the second
            i1 = 1;
            j1 = 0;
            i2 = 1;
            j2 = 1;
        } else {
            // zero is not in the beginning of the first row, exchange here
            i1 = 0;
            j1 = 0;
            i2 = 0;
            j2 = 1;
        }
        swap = twinBlocks[i1][j1];
        twinBlocks[i1][j1] = twinBlocks[i2][j2];
        twinBlocks[i2][j2] = swap;

        return new Board(twinBlocks);
    }

    /**
     * does this board2x2 equal that?
     *
     * @param that
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) o;

        if (that.blocks.length != this.blocks.length) {
            return false;
        }
        return Arrays.deepEquals(this.blocks, that.blocks);
    }

    /**
     * all neighboring boards
     *
     * @return
     */
    public Iterable<Board> neighbors() {
        return new Neighbors(blocks);
    }

    /**
     * string representation of this board2x2 The input and output format for a board2x2 is the
     * board2x2 dimension N followed by the N-by-N initial board2x2, using 0 to represent the blank
     * square. As an example,
     *
     * 3
     * 0 1 3
     * 4 2 5
     * 7 8 6
     *
     * @return
     */
    @Override
    public String toString() {
        int width = (int) Math.ceil(Math.log10(dimension() * dimension())) + 1;
        String format = "%" + width + "d";
        String result = String.format("%d", dimension());
        for (int i = 0; i < dimension(); i++) {
            result += String.format("%n");
            for (int j = 0; j < dimension(); j++) {
                result += String.format(format, blocks[i][j]);
            }
        }
        return result;
    }

    /**
     * unit tests
     *
     * @param args
     */
    public static void main(String[] args) {
        Test test = new Test("Board");
        Board board2x2 = new Board(new int[][]{{3, 2}, {1, 0}});

        Board board3x3 = new Board(new int[][]{{8, 7, 6}, {5, 4, 3}, {2, 1, 0}});

        test.that(board2x2.toString().equals("2\n 3 2\n 1 0"));

        test.that(1 == board2x2.getExpectedBlock(0, 0));
        test.that(2 == board2x2.getExpectedBlock(0, 1));
        test.that(3 == board2x2.getExpectedBlock(1, 0));
        test.that(0 == board2x2.getExpectedBlock(1, 1));

        test.that(board3x3.toString().equals("3\n 8 7 6\n 5 4 3\n 2 1 0"));

        test.that(1 == board3x3.getExpectedBlock(0, 0));
        test.that(2 == board3x3.getExpectedBlock(0, 1));
        test.that(3 == board3x3.getExpectedBlock(0, 2));
        test.that(4 == board3x3.getExpectedBlock(1, 0));
        test.that(5 == board3x3.getExpectedBlock(1, 1));
        test.that(6 == board3x3.getExpectedBlock(1, 2));
        test.that(7 == board3x3.getExpectedBlock(2, 0));
        test.that(8 == board3x3.getExpectedBlock(2, 1));
        test.that(0 == board3x3.getExpectedBlock(2, 2));

        test.that(2 == board2x2.hamming());
        test.that(2 == board2x2.manhattan());
        test.that(8 == board3x3.hamming());
        test.that(16 == board3x3.manhattan());

        // twins
        Board board;
        board = new Board(new int[][]{{0, 1}, {2, 3}});
        test.that(board.twin().toString().equals("2\n 0 1\n 3 2"));
        board = new Board(new int[][]{{1, 0}, {2, 3}});
        test.that(board.twin().toString().equals("2\n 1 0\n 3 2"));
        board = new Board(new int[][]{{1, 2}, {0, 3}});
        test.that(board.twin().toString().equals("2\n 2 1\n 0 3"));
        board = new Board(new int[][]{{1, 2}, {3, 0}});
        test.that(board.twin().toString().equals("2\n 2 1\n 3 0"));

        System.out.println(test);
        /*
         board3x3 = new Board(new int[][]{{1, 2, 3}, {4, 0, 5}, {6, 7, 8}});
         for (Board b : board3x3.neighbors()) {
         System.out.println(b);
         }
         */
    }

    /**
     * Get expected block for position (i, j)
     *
     * @param i
     * @param j
     * @return
     */
    private int getExpectedBlock(int i, int j) {
        return (dimension() * i + j + 1) % (dimension() * dimension());
    }
}
