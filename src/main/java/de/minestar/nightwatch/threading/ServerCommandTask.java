package de.minestar.nightwatch.threading;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.concurrent.Task;
import de.minestar.nightwatch.core.Core;

public class ServerCommandTask extends Task<Void> {

    private static final int CHECK_INTERVAL = 50;
    private LinkedBlockingQueue<String> commandQueue;
    private List<String> commandBuffer;
    private BufferedWriter serverInput;

    public ServerCommandTask(OutputStream serverInput, LinkedBlockingQueue<String> commandQueue) {
        this.commandQueue = commandQueue;
        this.commandBuffer = new ArrayList<>();
        this.serverInput = new BufferedWriter(new OutputStreamWriter(serverInput));
    }

    public void addCommand(String command) {
        this.commandQueue.add(command);
    }

    @Override
    protected Void call() throws Exception {
        while (!isCancelled()) {
            try {
                Thread.sleep(CHECK_INTERVAL);
            } catch (InterruptedException ignore) {
                // Only called, when thread is canceled while sleeping
            }
            flush();
        }
        return null;
    }

    public void flush() throws Exception {
        this.commandBuffer.clear();
        this.commandQueue.drainTo(commandBuffer);
        for (String command : commandBuffer) {
            Core.logger.debug(command);
            serverInput.write(command);
            serverInput.newLine();
            serverInput.flush();
        }
    }

    @Override
    protected void cancelled() {
        try {
            serverInput.close();
        } catch (IOException ignore) {
        }
    }

}
