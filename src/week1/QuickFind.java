package week1;

/**
 * Quick Find
 *
 * @author f3ath
 */
public class QuickFind extends UnionFind {

    public QuickFind(int n) {
        super(n);
    }

    @Override
    public boolean connected(int a, int b) {
        return id[a] == id[b];
    }

    @Override
    public void union(int a, int b) {
        int aid = id[a];
        int bid = id[b];
        for (int i = 0; i < id.length; i++) {
            if (id[i] == aid) {
                id[i] = bid;
            }
        }
    }
}
