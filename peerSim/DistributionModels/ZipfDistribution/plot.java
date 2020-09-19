import java.io.*;
import java.lang.Math;
import java.math.BigDecimal;

public class plot {
    public static void main(String[] args) {

        try {
            PrintWriter plot = new PrintWriter(new BufferedWriter(new FileWriter("./plot.tsv", false)));

            double s = -1.3;
            double total = 0.0;

            double denominator = 0.0;
            for (int id = 1; id <= 101; id++) {
                double n = (double) id;
                denominator += Math.pow(n, s);
            }

            for (int id = 1; id <= 501; id++) {
                double k = (double) id;
                double result = Math.pow(k, s) / denominator;

                int value = (int) Math.round(result * 990.0);
                total += value;
                System.out.println(value);
                plot.println(result);
            }
            System.out.println("total: " + total);
            plot.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}