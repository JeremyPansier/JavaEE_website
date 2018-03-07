package com.website.managers.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * The tool for uploading files from the user machine and download them to the server.</br>
 *
 * @author Jérémy Pansier
 */
public class Uploader {

	/** The logger. */
	private static final Logger LOGGER = LogManager.getLogger();

	/** The uploads directory name. */
	private static final String UPLOAD_DIRECTORY_NAME = "/Uploads/";

	/**
	 * The private constructor to hide the implicit empty public one.</br>
	 * This class should only be used on a static way.
	 */
	private Uploader() {}

	/**
	 * Uploads the file corresponding to.
	 *
	 * @param fileUploadEvent the file upload event corresponding to the file to upload
	 * @return the uploaded file name
	 */
	public static String uploadFile(final FileUploadEvent fileUploadEvent) {
		String fileName = null;

		try {
			fileUploadEvent.getSource();
			final UploadedFile uploadedFile = fileUploadEvent.getFile();

			if (uploadedFile != null) {
				fileName = uploadedFile.getFileName();
				final String temporaryFileName = "temp.tmp";
				final InputStream inputStream = uploadedFile.getInputstream();
				final File uploadsDirectory = new File(getHomePath() + UPLOAD_DIRECTORY_NAME);

				if (!uploadsDirectory.toPath().toFile().exists()) {
					Files.createDirectory(uploadsDirectory.toPath());
				}

				final File temporaryFile = new File(getHomePath() + UPLOAD_DIRECTORY_NAME + temporaryFileName);
				Files.copy(inputStream, temporaryFile.toPath());
				final File file = new File(getHomePath() + UPLOAD_DIRECTORY_NAME + fileName);

				if (!temporaryFile.renameTo(file)) {
					LOGGER.warn("File rename issue, aborted.");
				}

				Files.delete(temporaryFile.toPath());
			}
		}
		catch (final IOException e) {
			LOGGER.info("File copy issue, this file may already exists in the directory", e);
		}
		return fileName;
	}

	/**
	 * Gets the home path.
	 *
	 * @return the home path
	 */
	private static String getHomePath() {
		return System.getProperty("user.home");
	}
}
