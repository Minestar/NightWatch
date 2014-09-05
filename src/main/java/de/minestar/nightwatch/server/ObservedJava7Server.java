package de.minestar.nightwatch.server;

import java.io.File;
import java.util.List;

import de.minestar.nightwatch.core.Core;

public class ObservedJava7Server extends ObservedServer {

    private String permGenSize;

    protected ObservedJava7Server() {
        // For serialization
    }

    public ObservedJava7Server(String name, File serverFile, String minMemory, String maxMemory, String permGenSize) {
        super(name, serverFile, minMemory, maxMemory);
        this.permGenSize = permGenSize;
    }


    public String getPermGenSize() {
        return permGenSize;
    }

    @Override
    protected void buildCommands(List<String> processCommands) {
        super.buildCommands(processCommands);
        processCommands.set(0, Core.mainConfig.java7Path().get());
        processCommands.add(1, "-XX:MaxPermSize=" + getPermGenSize());
    }

}