package com.preludio.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.preludio.services.Context;
import com.preludio.services.Message;
import com.preludio.services.System;
import com.preludio.services.admin.event.Event;
import com.preludio.services.admin.event.EventPublisherService;
import com.preludio.services.infra.auth.Authtoken;
import com.preludio.services.infra.auth.AuthtokenService;
import com.preludio.services.infra.auth.exception.LoginException;
import com.preludio.services.profile.Profile;
import com.preludio.services.profile.ProfileService;

@SuppressWarnings("serial")
public class LoginServlet extends BaseServlet {

	public static final String HOME = "/";
	public static final String FEED = "/feed";
	public static final String LOGIN = "/login";

	public void __doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		Message request = new Message(new Context());
		request.put(Profile.EMAIL, req.getParameter(Profile.EMAIL));
		request.put(Profile.PASSWORD, req.getParameter(Profile.PASSWORD));

		Message response = new Message(request.getContext());

		try {
			new AuthtokenService().doPost(request, response);

			// retrieve user info
			request.put(Profile.ID, response.get(Profile.ID));
			new ProfileService().doGet(request, response);

			// create and add context to session
			Context ctx = new Context();
			ctx.setLoggedInUser(response.get(Profile.ID), response.get(Profile.FULL_NAME));
			req.getSession().setAttribute(Authtoken.TOKEN, ctx);

			// registers a successful login event
			Message eventRequest = new Message(request.getContext());
			eventRequest.put(Event.DOCUMENT_TYPE, Authtoken.RESOURCE_NAME);
			eventRequest.put(Event.TYPE, "POST");
			eventRequest.put(Event.DESCRIPTION, "Login");
			eventRequest.put(Event.OBJECT, response.get(Profile.ID));
			eventRequest.put(Event.USER, ctx.getLoggedInUserProfileId());
			eventRequest.put(Event.TIMESTAMP, request.get(System.TIMESTAMP));

			EventPublisherService pub = new EventPublisherService();
			pub.doPost(eventRequest, response);

			// edit performed, so redirect
			super.redirect(FEED, resp);
		} catch (LoginException e) {
			// unsuccessful login
			// no edit performed, so forward
			super.forward(LOGIN, req, resp);
		}
	}

	public void __doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Ouch... not implemented yet :(");
	}

	public void __doPut(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Ouch... not implemented yet :(");
	}

	public void __doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.getSession().removeAttribute(Authtoken.TOKEN);
		req.getSession().invalidate();

		// edit performed, so redirect
		super.redirect(HOME, resp);
	}
}
