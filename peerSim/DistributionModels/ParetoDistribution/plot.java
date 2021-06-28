import java.io.*;
import java.lang.Math;
import java.math.BigDecimal;
import java.util.Random;

public class plot {
    private static final double PARATE_SHAPE = 0.3;
    private static final int PARATE_MIN = 100;
    private static final int PARATE_MAX = 1000;
    private static final int TOTAL_CONTENT = 100000;
    private static int[] size = new int[TOTAL_CONTENT];
    private static Random random = new Random();

    private static void setSize(double[] cdf, int sizeCount) {
        int contentId = 0;
        while (contentId < TOTAL_CONTENT) {
            double rand = random.nextDouble();

            for (int cdfId = 0; cdfId < sizeCount; cdfId++) {
                if (rand < cdf[cdfId]) {
                    size[contentId] = cdfId;
                    System.out.println(size[contentId]);
                    break;
                }
            }

            contentId++;
        }
    }

    private static void paretoDistribution() {
        double alpha = PARATE_SHAPE;
        double min = (double) PARATE_MIN;
        double max = (double) PARATE_MAX + 1.0;
        int sizeCount = PARATE_MAX + 1;

        double cdf[] = new double[sizeCount];
        for (int id = 1; id <= sizeCount; id++) {
            double x = (double) id;
            cdf[id - 1] = ((1 - Math.pow(min, alpha) * Math.pow(x, -1.0 * alpha)) / (1 - Math.pow(min / max, alpha)));
        }

        setSize(cdf, sizeCount);
    }

    private static void buildGraph() {
        try {
            PrintWriter plot = new PrintWriter(new BufferedWriter(new FileWriter("./plot.tsv", false)));

            int range = 100;

            int[] counter = new int[PARATE_MAX + 1];
            for (int s : size) {
                // for (int i = PARATE_MIN; i < PARATE_MAX; i += range) {
                // if (i <= s && s <= i + range - 1) {
                // counter[i]++;
                // }
                // }
                counter[s]++;
            }

            for (int i = 0; i < counter.length; i++) {
                if (counter[i] > 0) {
                    System.out.println(i + ": " + counter[i]);
                }
                plot.println(counter[i]);
            }

            plot.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {

        paretoDistribution();
        buildGraph();
    }
}