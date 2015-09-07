package week1.quiz;

import week1.*;

/**
 * Quiz 1 question 1
 *
 * @author f3ath
 */
public class Q11 extends UFSolution {

    @Override
    UnionFind getUnionFind(int size) {
        return new QuickFind(size);
    }

    public static void main(String[] args) {
        UFSolution solution = new Q11();
        solution.solve(args);
    }
}
