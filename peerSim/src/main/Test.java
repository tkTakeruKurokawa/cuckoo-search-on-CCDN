package main;

import java.util.ArrayList;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

public class Test implements Control {
    private static final String PAR_TOTAL_CONTENTS = "totalContents";
    private static int totalContents;

    public Test(String prefix) {
        totalContents = Configuration.getInt(prefix + "." + PAR_TOTAL_CONTENTS);
    }

    public boolean execute() {
        System.out.println("==================================================================");

        CuckooSearch.runSearch(SharedData.getContent(0));

        System.out.println("==================================================================");

        return false;
    }
}