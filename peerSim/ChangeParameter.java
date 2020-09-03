import java.io.*;
import java.util.ArrayList;

public class ChangeParameter {
    private static String parameterName;
    private static double parameterValue;
    private static String directoryName;
    private static String name;
    private static String[] data;

    private static void run(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

            String line;
            ArrayList<String> outputs = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (filePath.contains("config")) {
                    line = checkAndReplaceConfig(line);
                }
                if (filePath.contains("plot.plot")) {
                    line = checkAndReplacePlot(line);
                }
                if (filePath.contains("proposed.plot")) {
                    line = checkAndReplaceProposed(line);
                }
                outputs.add(line);
            }
            reader.close();

            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File(filePath))));
            for (String output : outputs) {
                // System.out.println(output);
                writer.println(output);
            }
            writer.close();

        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static String checkAndReplaceConfig(String line) {
        String[] words = line.split(" ");
        if (words[0].equals(parameterName)) {
            return words[0] + " " + parameterValue;
        } else if (words[0].equals("init.sd.directoryName")) {
            return "init.sd.directoryName" + " " + directoryName;
        }

        return line;
    }

    private static String checkAndReplacePlot(String line) {
        String[] words = line.split("=");
        if (words[0].equals("filePath")) {
            return "filePath=" + "\"./" + directoryName + "\"";
        }

        return line;
    }

    private static String checkAndReplaceProposed(String line) {
        String[] words = line.split("=");
        if (words[0].equals("elements")) {
            return "elements=" + data.length;
        } else if (words[0].equals("directoryName")) {
            return "directoryName=" + "\"" + name + "\"";
        } else if (words[0].equals("array paths[elements]")) {
            String paths = "[";
            for (int i = 0; i < data.length; i++) {
                paths += "\"" + name + data[i] + "\"";
                if (i != data.length - 1) {
                    paths += ", ";
                }
            }
            paths += "]";

            return "array paths[elements]=" + paths;
        }

        return line;
    }

    public static void main(String[] args) {
        if (args.length == 3) {
            parameterName = String.valueOf(args[0]);
            parameterValue = Double.valueOf(args[1]);
            name = String.valueOf(args[2]);
            directoryName = name + args[1];

            File dir = new File("result/" + directoryName + "/eps");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            run("./src/main/config.txt");
            run("./result/plot.plot");
        } else {
            data = args[0].split(",");
            name = String.valueOf(args[1]);

            File dir = new File("result/proposed/" + name);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            run("./result/proposed.plot");
        }
    }
}