/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package week2.quiz;

import static java.lang.System.out;

/**
 * Determine if the sequence is a valid sequence of stack pop operations.
 * Assumed: push operations are 0, 1, 2, ..., 8, 9
 *
 * @author f3ath
 */
public class Q11 {

    public static boolean valid(String[] args) {
        boolean[] popped = new boolean[]{false, false, false, false, false, false, false, false, false, false};
        int p = 0;
        for (String s : args) {
            int n = Integer.parseInt(s);
            if (popped[n]) {
                return false;
            }
            popped[n] = true;
            if (n >= p) {
                p = n;
            } else {
                while (p > n) {
                    p--;
                    if (!popped[p]) {
                        return false;
                    }
                }
            }
        }
        return true;

    }

    public static void main(String[] args) {
        if (args.length < 1) {
            out.println("Parameters missing");
            return;
        }
        out.println(valid(args) ? "TRUE" : "FALSE!");
    }
}
