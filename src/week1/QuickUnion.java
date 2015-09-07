package week1;

/**
 * Weighted Quick Union w/o path compression
 *
 * @author f3ath
 */
public class QuickUnion extends UnionFind {

    private final int[] size;

    public QuickUnion(int n) {
        super(n);
        size = new int[n];
        for (int i = 0; i < n; i++) {
            size[i] = 1;
        }
    }

    private int root(int p) {
        while (id[p] != p) {
            p = id[p];
        }
        return p;
    }

    @Override
    public boolean connected(int a, int b) {
        return root(a) == root(b);
    }

    @Override
    public void union(int a, int b) {
        int p = root(a);
        int q = root(b);
        if (p == q) {
            return;
        }
        if (size[p] < size[q]) {
            id[p] = q;
            size[q] += size[p];
        } else {
            id[q] = p;
            size[p] += size[q];
        }
    }
}
