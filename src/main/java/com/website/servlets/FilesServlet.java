package com.website.servlets;

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

import com.website.tools.Uploader;

@WebServlet("/FilesServlet/*")
public class FilesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String fileName = URLDecoder.decode(request.getPathInfo().substring(1), "UTF-8");
			File file = Uploader.getFileDependingOnHomePath(fileName);
			response.setHeader("Content-Type", getServletContext().getMimeType(fileName));
			response.setHeader("Content-Length", String.valueOf(file.length()));
			Files.copy(file.toPath(), response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("File upload issue", e);
		}
	}

}
