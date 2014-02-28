package com.preludio.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.preludio.services.Context;
import com.preludio.services.Message;
import com.preludio.services.System;
import com.preludio.services.admin.event.Event;
import com.preludio.services.admin.event.EventPublisherService;
import com.preludio.services.infra.auth.AuthenticationInfoService;
import com.preludio.services.infra.auth.Authtoken;
import com.preludio.services.infra.auth.AuthtokenService;
import com.preludio.services.infra.data.Document;
import com.preludio.services.profile.Profile;
import com.preludio.services.profile.ProfileService;

@SuppressWarnings("serial")
public class ProfileServlet extends BaseServlet {

	public static final String FEED = "/feed";
	public static final String PROFILE_VIEW = "/profile/view";
	public static final String PROFILE_EDIT = "/profile/edit";

	public void __doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// signup
		Message request = new Message(new Context());
		request.put(Document.AUTHOR, "SYSTEM");
		request.put(Profile.FULL_NAME, req.getParameter(Profile.FULL_NAME));
		request.put(Profile.EMAIL, req.getParameter(Profile.EMAIL).toLowerCase());
		request.put(Profile.PASSWORD, req.getParameter(Profile.PASSWORD));

		Message response = new Message(request.getContext());

		new ProfileService().doPost(request, response);

		// create auth info
		request.put(Profile.PASSWORD, req.getParameter(Profile.PASSWORD));
		new AuthenticationInfoService().doPost(request, response);

		// authenticates (logs in) via token
		new AuthtokenService().doPost(request, response);

		request.remove(Profile.PASSWORD);

		if (response.get(System.RESULT_CODE).equals("-1")) {
			resp.setContentType("text/plain");
			resp.getWriter().println("Error: " + response.get(System.RESULT_MSG));
		} else {
			// retrieve user info
			request.put(Profile.ID, response.get(Profile.ID));
			new ProfileService().doGet(request, response);

			// create and add context to session
			Context ctx = new Context();
			ctx.setLoggedInUser(response.get(Profile.ID), response.get(Profile.FULL_NAME));
			req.getSession().setAttribute(Authtoken.TOKEN, ctx);

			// registers a signup event
			Message eventRequest = new Message(request.getContext());
			eventRequest.put(Event.DOCUMENT_TYPE, Profile.RESOURCE_NAME);
			eventRequest.put(Event.TYPE, "POST");
			eventRequest.put(Event.DESCRIPTION, "Signup");
			eventRequest.put(Event.OBJECT, response.get(Profile.ID));
			eventRequest.put(Event.USER, ctx.getLoggedInUserProfileId());
			eventRequest.put(Event.TIMESTAMP, request.get(System.TIMESTAMP));

			EventPublisherService pub = new EventPublisherService();
			pub.doPost(eventRequest, response);

			// edit performed, so redirect
			super.redirect(FEED, resp);
		}
	}

	public void __doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		Context ctx = (Context) req.getSession().getAttribute(Authtoken.TOKEN);
		if (ctx == null) {
			ctx = new Context();
		}

		String profileId = new String();
		if (req.getPathInfo() != null) {
			profileId = "/" + Profile.RESOURCE_NAME + "/" + req.getPathInfo().replaceAll("/", "");
		}

		if (!profileId.equals("")) {
			// retrieving specific profile
			Message request = new Message(ctx);
			request.put(Profile.ID, profileId);

			Message response = new Message(ctx);

			// retrieve profile
			new ProfileService().doGet(request, response);
			req.setAttribute(Profile.RESOURCE_NAME, response);

			// registers a profile view event
			Message eventRequest = new Message(request.getContext());
			eventRequest.put(Event.DOCUMENT_TYPE, Profile.RESOURCE_NAME);
			eventRequest.put(Event.TYPE, "GET");
			eventRequest.put(Event.DESCRIPTION, "View Profile");
			eventRequest.put(Event.OBJECT, request.get(Profile.ID));
			eventRequest.put(Event.USER, ctx.getLoggedInUserProfileId());
			eventRequest.put(Event.TIMESTAMP, request.get(System.TIMESTAMP));

			EventPublisherService pub = new EventPublisherService();
			pub.doPost(eventRequest, response);

			// no edit performed, so forward
			if (req.getParameter(Profile.EDITING) == null) {
				super.forward(PROFILE_VIEW, req, resp);
			} else {
				// editing requested
				if (profileId.equals(ctx.getLoggedInUserProfileId())) {
					super.forward(PROFILE_EDIT, req, resp);
				} else {
					super.forward(PROFILE_VIEW, req, resp);
				}
			}
		} else {
			resp.setContentType("text/plain");
			resp.getWriter().println("Ouch... not implemented yet :(");
		}
	}

	public void __doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Context ctx = (Context) req.getSession().getAttribute(Authtoken.TOKEN);

		Message request = new Message(new Context());
		request.put(Profile.ID, ctx.getLoggedInUserProfileId());
		request.put(Profile.DESCRIPTION, req.getParameter(Profile.DESCRIPTION));
		request.put(Profile.LOCATION, req.getParameter(Profile.LOCATION));

		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
		List<BlobKey> listOfBlobs = blobs.get(Profile.IMAGE);
		if (!listOfBlobs.isEmpty()) {
			BlobKey blobKey = listOfBlobs.get(0);
			if (blobKey != null) {
				BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
				if (blobInfo.getFilename().length() > 0){
					// picture uploaded, process blob
					request.put(Profile.IMAGE_KEY, blobKey.getKeyString());
				} else {
					// no picture uploaded
					blobstoreService.delete(blobKey);
				}
			}
		}

		Message response = new Message(request.getContext());

		new ProfileService().doPut(request, response);

		// registers a profile update event
		Message eventRequest = new Message(request.getContext());
		eventRequest.put(Event.DOCUMENT_TYPE, Profile.RESOURCE_NAME);
		eventRequest.put(Event.TYPE, "PUT");
		eventRequest.put(Event.DESCRIPTION, "Update Profile");
		eventRequest.put(Event.OBJECT, request.get(Profile.ID));
		eventRequest.put(Event.USER, ctx.getLoggedInUserProfileId());
		eventRequest.put(Event.TIMESTAMP, request.get(System.TIMESTAMP));

		EventPublisherService pub = new EventPublisherService();
		pub.doPost(eventRequest, response);

		// edit performed, so redirect
		super.redirect(ctx.getLoggedInUserProfileId(), resp);
	}

	public void __doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Ouch... not implemented yet :(");
	}
}
