package com.preludio.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public class ImageServerServlet extends BaseServlet {

	@Override
	protected void __doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String imageId = new String();
		if (req.getPathInfo() != null) {
			imageId = req.getPathInfo().replaceAll("/", "");
		}

		if (!imageId.equals("")) {
			BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	        blobstoreService.serve(new BlobKey(imageId), resp);
		} else {
			resp.setContentType("text/plain");
			resp.getWriter().println("Invalid request");
		}
	}

	@Override
	protected void __doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Ouch... not implemented yet :(");
	}

	@Override
	protected void __doPut(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Ouch... not implemented yet :(");
	}

	@Override
	protected void __doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Ouch... not implemented yet :(");
	}

}
