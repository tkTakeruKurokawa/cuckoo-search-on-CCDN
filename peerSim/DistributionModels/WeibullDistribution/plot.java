import java.io.*;

public class plot {
    public static void main(String[] args) {

        try {
            PrintWriter plot = new PrintWriter(new BufferedWriter(new FileWriter("./plot.tsv", false)));

            double k = 1.0;
            double lambda = 500.0;

            for (int i = 0; i < 500; i++) {
                double x = (double) i;
                double result = (k / lambda) * Math.pow((x / lambda), k - 1.0)
                        * Math.exp(-1.0 * Math.pow(x / lambda, k));
                System.out.println(result);
                plot.println(result);
            }
            plot.close();

        } catch (Exception e) {
        }

    }
}