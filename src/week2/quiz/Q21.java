/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package week2.quiz;

/**
 *
 * @author f3ath
 */
public class Q21 {

    static class InsertionSort {

        private final int[] v;

        public InsertionSort(int[] values) {
            v = values;
        }

        @Override
        public String toString() {
            String s = v[0] + "";
            for (int i = 1; i < v.length; i++) {
                s = s + " " + v[i];
            }
            return s;
        }

        private int findMax(int start) {
            int max = start;
            for (int i = start + 1; i < v.length; i++) {
                if (v[i] > v[max]) {
                    max = i;
                }
            }
            return max;
        }
        
        private void swap(int i , int j) {
            int x = v[i];
            v[i] = v[j];
            v[j] = x;
        }

        public void solve() {
            for (int i = 0; i < v.length; i++) {
                for (int j = i; j > 0; j-- ) {
                    if (v[j - 1] > v[j]) {
                        swap(j - 1, j);
                        System.out.println(this);
                    }
                }
            }
        }
    }
    
    public static void main(String[] args) {
        int[] v = new int[args.length];
        int i = 0;
        for (String s : args) {
            v[i++] = Integer.parseInt(s);
        }
        InsertionSort sort = new InsertionSort(v);
        System.out.println(sort);
        sort.solve();
    }

}
