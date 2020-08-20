package main;

import peersim.cdsim.CDState;
import peersim.core.Control;

public class Content implements Control {
    private int index;
    private int[] requests;

    public Content(int index) {
        this.index = index;
        this.requests = Parameters.getRequests(index);
    }

    public int getContentId() {
        return index;
    }

    public int getSize() {
        return Parameters.getSize(index);
    }

    public int getRequest() {
        return this.requests[CDState.getCycle()];
    }

    public double getPopularity() {
        return Parameters.getPopularity(index);
    }

    @Override
    public boolean execute() {

        return false;
    }

}