package week1;
/**
 * @author f3ath
 */
public abstract class UnionFind {

    protected final int[] id;

    public UnionFind(int n) {
        id = new int[n];
        for (int i = 0; i < id.length; i++) {
            id[i] = i;
        }
    }

    public abstract boolean connected(int a, int b);
    public abstract void union(int a, int b);

    @Override
    public String toString() {
        String s = id[0] + "";
        for (int i = 1; i < id.length; i++) {
            s = s + " " + id[i];
        }
        return s;
    }
}
