package main;

import java.util.ArrayList;

import peersim.cdsim.CDState;
import peersim.config.Configuration;
import peersim.core.Control;

public class Upload implements Control {
    private static final String PAR_TOTAL_CONTENTS = "totalContents";
    private static int totalContents;

    public Upload(String prefix) {
        totalContents = Configuration.getInt(prefix + "." + PAR_TOTAL_CONTENTS);
    }

    @Override
    public boolean execute() {

        ArrayList<Content> uploadContents = new ArrayList<Content>();
        for (int contentId = 0; contentId < totalContents; contentId++) {
            Content content = SharedData.getContent(contentId);

            if (content.uploadCycle() == CDState.getCycle()) {
                uploadContents.add(content);
            }
        }

        SharedData.setReplicatedContents(uploadContents);

        return false;
    }

}