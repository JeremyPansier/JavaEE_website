package com.aiconoa.trainings.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

public class Tools {
    private static final Logger LOGGER = LogManager.getLogger();

    private Tools() {
        super();
    }

    public static String uploadMyFile(HttpServletRequest request) {
        String fileName = null;
        try {
            Part filePart = request.getPart("file");
            if (filePart != null) {
                InputStream is = filePart.getInputStream();
                fileName = filePart.getSubmittedFileName();
                File outputFile = new File("/uploads", fileName);
                Files.copy(is, outputFile.toPath());
            }
        } catch (IOException | ServletException e) {
            LOGGER.info("probleme avec la copie du fichier, On continue quand même", e);
        }
        return fileName;
    }

    public static String uploadMyFile(FileUploadEvent fue) {
        String fileName = null;
        try {
            UploadedFile uploadedFile = fue.getFile();
            if (uploadedFile != null) {
                fileName = uploadedFile.getFileName();
                InputStream is = uploadedFile.getInputstream();
                File outputFile;
                if (isWindows()) {
                    outputFile = new File("/uploads", fileName);
                } else {
                    outputFile = new File("/home/ec2-user/uploads", fileName);
                }
                Files.copy(is, outputFile.toPath());
            }
        } catch (IOException e) {
            LOGGER.info("probleme avec la copie du fichier, On continue quand même", e);
        }
        return fileName;
    }

    public static boolean isWindows() {
        boolean b = false;
        String os = System.getProperty("os.name");
        // compilation de la regex
        Pattern p = Pattern.compile("([a-zA-Z])+");
        // création d'un moteur de recherche
        Matcher m = p.matcher(os);
        // lancement de la recherche de toutes les occurrences successives
        while (m.find()) {
            // comparaison de la sous-chaîne capturée
            b = m.group().compareTo("Windows") == 0;
        }
        return b;
    }
}
