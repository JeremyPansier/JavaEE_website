package com.website.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

public class Uploader {
    private static final Logger LOGGER = LogManager.getLogger();

    private Uploader() {
        super();
    }

    public static String uploadFile(FileUploadEvent fue) {
        String filename = null;
        try {
            fue.getSource();
            UploadedFile uploadedFile = fue.getFile();
            if (uploadedFile != null) {
                filename = uploadedFile.getFileName();
                String tempname = "temp.tmp";
                InputStream is = uploadedFile.getInputstream();
    			File temp = new File(getHomePath() + "/Uploads/" + tempname);
                Files.copy(is, temp.toPath());
                File file = new File(getHomePath() + "/Uploads/" + filename);
                temp.renameTo(file);
                temp.delete();
            }
        } catch (IOException e) {
            LOGGER.info("File copy issue, this file may already exists in the directory", e);
        }
        return filename;
    }

    public static String getHomePath() {
        return  System.getProperty("user.home");
    }
    public static File getFileDependingOnHomePath(String filename) {
        return new File(System.getProperty("user.home") + "/Uploads", filename);
    }
}
