package main;

import java.util.ArrayList;

import peersim.config.Configuration;
import peersim.core.Control;

public class Contents implements Control {
    private static final String PAR_TOTAL_CONTENTS = "totalContents";
    private static int totalContents;

    private static ArrayList<Contents> contents;

    private int index;
    private int[] requests;

    private Contents(int index) {
        this.index = index;
        this.requests = Parameters.getRequests(index);
    }

    public Contents(String prefix) {
        totalContents = Configuration.getInt(prefix + "." + PAR_TOTAL_CONTENTS);
        contents = new ArrayList<Contents>();
    }

    public static Contents getContent(int index) {
        return contents.get(index);
    }

    public int getContentId() {
        return index;
    }

    public double getPopularity(int contentId) {
        return Parameters.getPopularity(contentId);
    }

    public double getSize(int contentId) {
        return Parameters.getSize(contentId);
    }

    public int getRequest(int cycle) {
        return this.requests[cycle];
    }

    @Override
    public boolean execute() {

        for (int index = 0; index < totalContents; index++) {
            contents.add(index, new Contents(index));
        }

        return false;
    }

}