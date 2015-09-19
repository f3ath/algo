package week1.quiz;

/**
 * Quiz 2 question 1
 * @author f3ath
 */
public class Q21 {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: <input N-1> <input N> <seconds N-1> <seconds N>");
            return;
        }
        
        // input length growth ratio is logarithm base
        double logBase = Double.parseDouble(args[1]) / Double.parseDouble(args[0]);
        
        // time growth ratio
        double ratio = Double.parseDouble(args[3]) / Double.parseDouble(args[2]);
        
        // The math is simple: logBase^x = ratio => x = log(logBase, ratio) 
        System.out.println(Math.round(100 * Math.log(ratio) / Math.log(logBase)) / 100.0);
    }
    
}
