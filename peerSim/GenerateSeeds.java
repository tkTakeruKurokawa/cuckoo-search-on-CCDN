import java.io.*;
import java.util.Random;

public class GenerateSeeds {
    public static void main(String[] args) {
        switch (args.length) {
            case 0:
                System.out.println("第1引数：1試行内のシード数\n第2引数：試行回数");
                System.exit(0);
                break;

            case 1:
                System.out.println("第2引数：試行回数");
                System.exit(0);
                break;

            default:
                break;
        }

        Random random = new Random();
        int totalSeed = Integer.valueOf(args[0]);
        int totalTry = Integer.valueOf(args[1]);

        for (int tryCount = 0; tryCount < totalTry; tryCount++) {
            int[] seeds = new int[totalSeed];

            for (int seedCount = 0; seedCount < totalSeed; seedCount++) {
                seeds[seedCount] = random.nextInt();
            }

            generateSeed(tryCount, seeds);
        }
    }

    private static void generateSeed(int id, int[] seeds) {
        try {
            PrintWriter writer = new PrintWriter(
                    new BufferedWriter(new FileWriter("./src/main/Seeds/Seed" + id + ".txt", false)));

            for (int seed : seeds) {
                writer.println(seed);
            }

            writer.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }
}
