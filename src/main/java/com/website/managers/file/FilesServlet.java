package com.website.managers.file;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The files servlet.</br>
 * Lets to use files stored on the server.
 *
 * @author Jérémy Pansier
 */
@WebServlet("/FilesServlet/*")
public class FilesServlet extends HttpServlet {

	/** The serial version UID. */
	private static final long serialVersionUID = 3941269080059840455L;

	/** The logger. */
	private static final Logger LOGGER = LogManager.getLogger();

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse) */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		try {
			final String filename = URLDecoder.decode(request.getPathInfo().substring(1), "UTF-8");
			final File file = Uploader.createUploadedFile(filename);
			response.setHeader("Content-Type", getServletContext().getMimeType(filename));
			response.setHeader("Content-Length", String.valueOf(file.length()));
			Files.copy(file.toPath(), response.getOutputStream());
		}
		catch (final Exception e) {
			LOGGER.error("File upload issue", e);
		}
	}
}
