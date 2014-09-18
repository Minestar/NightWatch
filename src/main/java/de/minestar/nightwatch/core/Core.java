package de.minestar.nightwatch.core;

import java.io.File;

import javafx.application.Application;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

import de.minestar.nightwatch.gui.MainGUI;
import de.minestar.nightwatch.server.ServerManager;

/**
 * The main class of the application.
 */
public class Core {

    /**
     * The parser and writer for JSON
     */
    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    static {
        // Configure the JSON mapper
        JSON_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        JSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JSON_MAPPER.registerModule(new JSR310Module());
    }

    private static final File SERVER_LIST_FILE = new File("servers.json");
    /**
     * Hold all registered servers and responsible for persisting changes
     */
    public static final ServerManager serverManager;

    private static final File MAIN_CONFIG_FILE = new File("mainConfig.json");
    public static Configuration mainConfig;

    public static int runningServers = 0;

    static {
        // Initialize all final objects
        serverManager = new ServerManager(SERVER_LIST_FILE);
        try {
            mainConfig = Configuration.create(MAIN_CONFIG_FILE);
        } catch (Exception e) {
            System.err.println("Error while loading main config: ");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Application.launch(MainGUI.class, args);
    }

}
