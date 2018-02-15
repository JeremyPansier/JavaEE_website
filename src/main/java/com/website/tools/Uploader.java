package com.website.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

public class Uploader
{
	private static final Logger LOGGER = LogManager.getLogger();

	private static final String UPLOAD_DIRECTORY_NAME = "/Upload/";

	public static String uploadFile(final FileUploadEvent fue)
	{
		String fileName = null;
		try
		{
			fue.getSource();
			final UploadedFile uploadedFile = fue.getFile();
			if (uploadedFile != null)
			{
				fileName = uploadedFile.getFileName();
				final String temporaryFileName = "temp.tmp";
				final InputStream inputStream = uploadedFile.getInputstream();
				final File uploadsDirectory = new File(getHomePath() + UPLOAD_DIRECTORY_NAME);
				if (!uploadsDirectory.toPath().toFile().exists())
				{
					Files.createDirectory(uploadsDirectory.toPath());
				}
				final File temporaryFile = new File(getHomePath() + UPLOAD_DIRECTORY_NAME + temporaryFileName);
				Files.copy(inputStream, temporaryFile.toPath());
				final File file = new File(getHomePath() + UPLOAD_DIRECTORY_NAME + fileName);
				if (!temporaryFile.renameTo(file))
				{
					LOGGER.warn("File rename issue, aborted.");
				}
				Files.delete(temporaryFile.toPath());
			}
		}
		catch (final IOException e)
		{
			LOGGER.info("File copy issue, this file may already exists in the directory", e);
		}
		return fileName;
	}

	private static String getHomePath()
	{
		return System.getProperty("user.home");
	}
}
