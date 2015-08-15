
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Board puzzle solver
 *
 * @author f3ath
 */
public class Solver {

    /**
     * Game tree node
     */
    private class Node {

        public Node parent;
        public final Board board;
        public final int level;

        public Node(Board myBoard, int myLevel) {
            board = myBoard;
            level = myLevel;
        }
    }

    private class SolutionIterator implements Iterator<Board> {

        private final Board[] moves;
        private int i = 0;

        public SolutionIterator(Board[] moves) {
            this.moves = moves;
        }

        @Override
        public boolean hasNext() {
            return i < moves.length;
        }

        @Override
        public Board next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return moves[i++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class Solution implements Iterable<Board> {

        private final Board[] moves;

        public Solution(Board[] moves) {
            this.moves = moves;
        }

        @Override

        public Iterator<Board> iterator() {
            return new SolutionIterator(moves);
        }

    }

    private static class Manhattan implements Comparator<Node> {

        @Override
        public int compare(Node a, Node b) {
            return compareInt(a.level + a.board.manhattan(), b.level + b.board.manhattan());
        }

        private int compareInt(int a, int b) {
            if (a > b) {
                return 1;
            } else if (a < b) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private static class Hamming implements Comparator<Node> {

        @Override
        public int compare(Node a, Node b) {
            return compareInt(a.level + a.board.hamming(), b.level + b.board.hamming());
        }

        private int compareInt(int a, int b) {
            if (a > b) {
                return 1;
            } else if (a < b) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private final MinPQ<Node> gamePQ, twinPQ;

    /**
     * Either Manhattan or Hamming
     */
    private static final Comparator<Node> COMPARATOR = new Manhattan();
    private final Solution solution;

    /**
     * find a solution to the initial board (using the A* algorithm)
     *
     * @param initial
     */
    public Solver(Board initial) {
        gamePQ = new MinPQ<>(COMPARATOR);
        twinPQ = new MinPQ<>(COMPARATOR);
        gamePQ.insert(new Node(initial, 0));
        twinPQ.insert(new Node(initial.twin(), 0));

        while (!gamePQ.min().board.isGoal() && !twinPQ.min().board.isGoal()) {
            process(gamePQ);
            process(twinPQ);
        }
        Node node = gamePQ.min();
        if (node.board.isGoal()) {
            Board[] moves = new Board[node.level + 1];
            do {
                moves[node.level] = node.board;
                node = node.parent;
            } while (node != null);
            solution = new Solution(moves);
        } else {
            solution = null;
        }
    }

    /**
     * is the initial board solvable?
     *
     * @return
     */
    public boolean isSolvable() {
        return gamePQ.min().board.isGoal();
    }

    /**
     * min number of moves to solve initial board; -1 if unsolvable
     *
     * @return
     */
    public int moves() {
        if (isSolvable()) {
            return gamePQ.min().level;
        } else {
            return -1;
        }
    }

    /**
     * sequence of boards in a shortest solution; null if unsolvable
     *
     * @return
     */
    public Iterable<Board> solution() {
        if (solution == null) {
            return null;
        } else {
            return solution;
        }
    }

    /**
     * solve a slider puzzle
     *
     * @param args
     */
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }

    private void process(MinPQ<Node> pq) {
        Node node = pq.delMin();
        for (Board neighbor : node.board.neighbors()) {
            if (node.parent != null && node.parent.board.equals(neighbor)) {
                continue;
            }
            Node child = new Node(neighbor, node.level + 1);
            child.parent = node;
            pq.insert(child);
        }
    }

    private void log(String msg) {
        System.out.println(msg);
    }
}
