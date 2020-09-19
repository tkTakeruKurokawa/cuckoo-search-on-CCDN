import java.io.*;
import java.lang.Math;
import java.math.BigDecimal;
import java.util.Random;

public class plot {
    public static void main(String[] args) {

        try {
            PrintWriter plot = new PrintWriter(new BufferedWriter(new FileWriter("./plot.tsv", false)));

            double alpha = 0.5;
            double min = 1.0;
            double max = 101.0;
            double total = 0.0;

            Random random = new Random(1L);

            int num = 101;

            double[] size = new double[num];
            for (int i = 1; i <= num; i++) {
                double x = (double) i;
                // double result = (alpha * Math.pow(1, alpha) / Math.pow(x, alpha + 1));
                size[i - 1] = ((1 - Math.pow(min, alpha) * Math.pow(x, -1.0 * alpha))
                        / (1 - Math.pow(min / max, alpha)));
                // size[i - 1] = ((alpha * Math.pow(min, alpha) * Math.pow(x, -1.0 * alpha -
                // 1.0))
                // / (1.0 - Math.pow(min / max, alpha)));
                total += size[i - 1];
                System.out.println(i - 1 + ": " + size[i - 1]);
                // plot.println(size[i - 1]);
            }
            System.out.println("total: " + total);
            System.out.println("======================================================");

            double[] useSize = new double[num];
            int i = 0;
            int sum = 0;
            while (i < 500) {
                double rand = random.nextDouble();
                boolean flag = false;

                for (int id = 0; id < num; id++) {

                    if (rand < size[id]) {
                        useSize[id]++;
                        sum++;
                        break;
                    } else {
                    }
                }

                if (flag) {
                    continue;
                }
                i++;
            }

            total = 0.0;
            for (int j = 0; j < num; j++) {
                if (useSize[j] > 0.0) {
                    System.out.println(j + ": " + useSize[j]);
                }
                plot.println(useSize[j]);
                total += useSize[j];
            }
            System.out.println("total: " + total);
            plot.close();

        } catch (

        Exception e) {
            System.out.println(e);
        }

    }
}