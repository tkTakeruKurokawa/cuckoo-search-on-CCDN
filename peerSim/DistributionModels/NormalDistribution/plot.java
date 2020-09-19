import java.io.*;
import java.lang.Math;
import org.apache.commons.math3.special.Erf;

public class plot {
    public static void main(String[] args) {

        try {
            PrintWriter plot = new PrintWriter(new BufferedWriter(new FileWriter("./plot.tsv", false)));

            double mu = 20;
            double sigma = 20;
            int total = 0;

            double[] cdf = new double[30];
            for (int i = 10; i <= 30; i++) {
                double x = (double) i;
                // double result = (1 / (Math.sqrt(2.0 * Math.PI * sigma)))
                // * Math.exp(-1 * (Math.pow(x - mu, 2.0) / (2.0 * sigma)));
                cdf[i - 1] = (1.0 / 2.0) * (1.0 + Erf.erf((x - mu) / Math.sqrt(2.0 * sigma)));
                System.out.println(i + ", " + cdf[i - 1]);
            }
            System.out.println("===============================");

            int[] count = new int[30];
            int num = 1000;
            int index = 0;
            while (index < num) {
                double rand = Math.random();
                boolean flag = false;

                for (int i = 0; i < cdf.length; i++) {
                    if (rand < cdf[i]) {
                        count[i]++;
                        break;
                    } else {
                        if (i == cdf.length - 1) {
                            flag = true;
                            System.out.println(i + ", " + rand);
                        }
                    }
                }

                if (flag) {
                    continue;
                }

                index++;
            }

            for (int i = 9; i < count.length; i++) {
                System.out.println((i + 1) / 10.0 + ", " + count[i]);
                plot.println((i + 1) / 10.0 + "\t" + count[i]);
            }
            // System.out.println(" " + total);
            plot.close();

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}