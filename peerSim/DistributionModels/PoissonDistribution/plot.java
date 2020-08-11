import java.io.*;
import java.lang.Math;
import java.math.BigDecimal;
import java.util.Random;

public class plot {

    public static double factorial(int src) {
        if (src == 0) {
            return ((double) 1.0);
        }
        double value = 1;
        for (int i = 1; i <= src; i++) {
            value *= i;
        }
        // System.out.println(value);
        return ((double) value);
    }

    public static void main(String[] args) {

        try {
            PrintWriter plot = new PrintWriter(new BufferedWriter(new FileWriter("./plot.tsv", false)));

            int num = 10;
            double lambda = (double) num;

            double[] cdf = new double[num * 2 + 1];
            for (int i = 0; i <= num * 2; i++) {
                double p = 0.0;

                for (int j = 0; j <= i; j++) {
                    p += ((Math.pow(lambda, j)) / (factorial(j)));
                }
                cdf[i] = p * Math.exp(-1.0 * lambda);
                // cdf[i] = Math.exp(-1 * lambda) * (Math.pow(lambda, cycle)) / factorial(i);
                System.out.println(i + ": " + cdf[i]);
            }
            System.out.println("===============================================");

            int count = 0;
            int total = 0;
            int[] list = new int[100];
            while (count < 1) {
                double rand = Math.random();
                System.out.println("rand: " + rand);

                boolean flag = false;
                for (int cdfId = 0; cdfId <= num * 2; cdfId++) {
                    if (rand < cdf[cdfId]) {
                        if (cdfId == 0) {
                            flag = true;
                            System.out.println(rand);
                            break;
                        }
                        total++;
                        list[cdfId]++;
                        break;
                    } else {
                        if (cdfId == num * 2) {
                            flag = true;
                        }
                    }
                }

                if (flag) {
                    continue;
                }

                count++;
            }

            for (int i = 0; i < 100; i++) {
                if (list[i] > 0) {
                    System.out.println(i + ", " + list[i]);
                    plot.println(i + ", " + list[i]);
                }
            }
            System.out.println(total);
            plot.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}