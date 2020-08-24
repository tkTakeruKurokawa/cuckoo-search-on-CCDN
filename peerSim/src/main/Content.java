package main;

import peersim.cdsim.CDState;
import peersim.core.Control;

public class Content implements Control {
    private int index;
    private int uploadCycle;
    private int[] requests;

    public Content(int index) {
        this.index = index;
        this.uploadCycle = Parameters.getUploadCycle();
        this.requests = Parameters.getRequests(index, uploadCycle);
    }

    public int getContentId() {
        return index;
    }

    public int getSize() {
        return Parameters.getSize(index);
    }

    public int uploadCycle() {
        return uploadCycle;
    }

    public int getRequest() {
        return requests[CDState.getCycle()];
    }

    public double getPopularity() {
        return Parameters.getPopularity(index);
    }

    @Override
    public boolean execute() {

        return false;
    }

}