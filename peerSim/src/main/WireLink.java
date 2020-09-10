package main;

import java.io.*;

import peersim.config.Configuration;
import peersim.dynamics.*;
import peersim.graph.*;

public class WireLink extends WireGraph {
    private static final String PAR_PROT = "protocol";
    private final int pid;
    private static final String PAR_TOTAL_NODES = "totalNodes";
    private static int totalNodes;
    private Graph g;

    public WireLink(String prefix) {
        super(prefix);
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
        totalNodes = Configuration.getInt(prefix + "." + PAR_TOTAL_NODES);
    }

    public void wire(Graph g) {
        this.g = g;

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream("./src/main/" + totalNodes + "NodesNetwork")));

            String line;
            String words[] = new String[4];
            while ((line = br.readLine()) != null) {
                words = line.split(" ");

                int source = Integer.valueOf(words[0]);
                int destination = Integer.valueOf(words[1]);

                g.setEdge(source, destination);
                g.setEdge(destination, source);
            }
            br.close();

        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}