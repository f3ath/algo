package week1.quiz;

/**
 * Quiz 1 question 3
 *
 * @author f3ath
 */
public class Q13 {

    static class CircularReferenceException extends RuntimeException {
    }

    /**
     * Given id layout
     */
    private final int[] id;

    private Q13(int[] id) {
        this.id = id;
    }

    /**
     * Is layout possible
     *
     * @return
     */
    private boolean isPossible() {

        // Subtree sizes
        int[] size = new int[id.length];

        // Find root nodes, Calculate subtree sizes recursively
        try {
            for (int i = 0; i < id.length; i++) {
                if (id[i] == i) {
                    calculateSize(i, size);
                }
            }
        } catch (CircularReferenceException e) {
            return false;
        }

        // For all non-root nodes make sure node's size has been calculated
        // and is not greater than parent's size
        for (int i = 0; i < id.length; i++) {
            if (id[i] != i) {
                if (size[i] == 0 || size[i] * 2 > size[id[i]]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void calculateSize(int node, int[] size) {
        if (size[node] > 0) {
            throw new CircularReferenceException();
        }
        int s = 1;
        size[node] = 1;
        for (int i = 0; i < id.length; i++) {
            if (id[i] == node && i != node) {
                calculateSize(i, size);
                s += size[i];
            }
        }
        size[node] = s;
    }

    public static void main(String[] args) {
        int[] id = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            id[i] = Integer.parseInt(args[i]);
        }
        Q13 solution = new Q13(id);
        System.out.println(solution.isPossible() ? "possible" : "NOT possible");
    }

}
