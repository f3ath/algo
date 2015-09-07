package week1.quiz;

import week1.*;

/**
 * Quiz 1 question 2
 *
 * @author f3ath
 */
public class Q12 extends UFSolution {

    @Override
    UnionFind getUnionFind(int size) {
        return new QuickUnion(size);
    }

    public static void main(String[] args) {
        UFSolution solution = new Q12();
        solution.solve(args);
    }
}
