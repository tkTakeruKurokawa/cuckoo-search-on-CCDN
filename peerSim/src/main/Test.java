package main;

import peersim.cdsim.CDState;
import peersim.config.Configuration;
import peersim.core.Control;

public class Test implements Control {
    private static final String PAR_TOTAL_CONTENTS = "totalContents";
    private static int totalContents;

    public Test(String prefix) {
        totalContents = Configuration.getInt(prefix + "." + PAR_TOTAL_CONTENTS);
    }

    public boolean execute() {

        for (int i = 0; i < totalContents; i++) {
            Contents content = Contents.getContent(i);
            // System.out.println(i + ": size=" + content.getSize(i) + ", total=" +
            // Parameters.getTotalRequest(i));

            int cycle = CDState.getCycle();
            if (content.getRequest(cycle) > 0) {

                System.out.println(i + ", " + "request=" + content.getRequest(cycle));
            }
        }

        return false;
    }
}