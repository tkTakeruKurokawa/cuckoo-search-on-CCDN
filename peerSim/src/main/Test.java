package main;

import java.math.BigDecimal;
import java.util.ArrayList;

import peersim.cdsim.CDState;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

public class Test implements Control {
    private static final String PAR_TOTAL_CONTENTS = "totalContents";
    private static int totalContents;

    private static double[] averageFailure = new double[100];

    public Test(String prefix) {
        totalContents = Configuration.getInt(prefix + "." + PAR_TOTAL_CONTENTS);
    }

    public boolean execute() {

        for (int i = 0; i < totalContents; i++) {
            System.out.println("==================================================================");

            Content content = SharedData.getContent(i);
            System.out.println("Content: " + content.getContentId() + ", Popularity: " + content.getPopularity()
                    + ", Size: " + content.getSize());
            CuckooSearch.runSearch(content);
            System.out.println("==================================================================");
        }

        return false;
    }
}