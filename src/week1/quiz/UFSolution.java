package week1.quiz;

import week1.*;

/**
 * Quiz 1 question 1 and 2
 *
 * @author f3ath
 */
abstract public class UFSolution {

    abstract UnionFind getUnionFind(int size);

    public void solve(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: <size> <a>-<b> ... <a>-<b>");
            return;
        }
        UnionFind uf = getUnionFind(Integer.parseInt(args[0]));
        for (int i = 1; i < args.length; i++) {
            String[] pair = args[i].split("-");
            uf.union(Integer.parseInt(pair[0]), Integer.parseInt(pair[1]));
        }
        System.out.println("Ids: " + uf);
    }
}
