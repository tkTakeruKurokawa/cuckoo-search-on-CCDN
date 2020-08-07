import java.io.*;
import java.lang.Math;

public class plot {
    public static void main(String[] args) {

        try {
            PrintWriter plot = new PrintWriter(new BufferedWriter(new FileWriter("./plot.tsv", false)));

            double mu = 5.0;
            double sigma = 3;
            int total = 0;

            for (int i = 1; i < 10; i++) {
                double x = (double) i;
                double result = (1 / (Math.sqrt(2.0 * Math.PI * sigma)))
                        * Math.exp(-1 * (Math.pow(x - mu, 2.0) / (2.0 * sigma)));
                int value = (int) Math.round(result * 100.0);
                total += value;
                System.out.printf("%d, ", value);
                plot.println(i + "\t" + value);
            }
            System.out.println("   " + total);
            plot.close();

        } catch (Exception e) {
        }

    }
}