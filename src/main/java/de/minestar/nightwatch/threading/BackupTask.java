package de.minestar.nightwatch.threading;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import de.minestar.nightwatch.server.ObservedServer;
import de.minestar.nightwatch.util.FileCountVisitor;
import de.minestar.nightwatch.util.ZipFileVisitor;

public class BackupTask extends Task<Void> {

    public static final DateTimeFormatter BACKUP_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH.mm.ss");

    private ObservedServer server;
    private File backupDirectory;

    public BackupTask(ObservedServer server, File backupDirectory) {
        this.server = server;
        this.backupDirectory = backupDirectory;
    }

    @Override
    protected Void call() throws Exception {

        File source = server.getDirectory();

        updateMessage("Count Files");
        updateProgress(0, 1);

        int filesCount = FileCountVisitor.count(source);
        updateMessage("Files to proceed: " + filesCount);
        updateProgress(1, filesCount);
        updateMessage("Start backup");
        IntegerProperty procededFiles = new SimpleIntegerProperty();
        procededFiles.addListener((observ, oldVal, newVal) -> {
            updateProgress(procededFiles.get(), filesCount);
            updateMessage("Backup file " + procededFiles.get() + " of " + filesCount);
        });

        String timestampString = LocalDateTime.now().format(BACKUP_TIME_FORMAT);
        String fileName = server.getName() + "_" + timestampString;
        File targetFile = new File(backupDirectory, fileName+".incomplete");
        ZipFileVisitor.zipDirWithProgress(source, targetFile, procededFiles);
        targetFile.renameTo(new File(backupDirectory, fileName + ".zip"));
        updateMessage("Backup complete");
        

        return null;
    }
}
