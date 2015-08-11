/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author f3ath
 */
public class Subset {

    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }
        for (int i = 0; i < Integer.parseInt(args[0]); i++) {
            System.out.println(queue.dequeue());
        }
    }
}
