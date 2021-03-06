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
import com.preludio.services.answer.Answer;
import com.preludio.services.answer.vote.Vote;
import com.preludio.services.answer.vote.VoteService;
import com.preludio.services.infra.auth.Authtoken;
import com.preludio.services.infra.data.Document;
import com.preludio.services.profile.Profile;
import com.preludio.services.question.Question;

@SuppressWarnings("serial")
public class VoteServlet extends BaseServlet {

	public void __doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Context ctx = (Context) req.getSession().getAttribute(Authtoken.TOKEN);

		Message request = new Message(ctx);
		request.put(Document.AUTHOR, ctx.getLoggedInUserProfileId());
		request.put(Answer.ID, req.getParameter(Answer.ID));
		request.put(Profile.ID, ctx.getLoggedInUserProfileId());
		request.put(Vote.VOTE, req.getParameter(Vote.VOTE));

		Message response = new Message(ctx);

		new VoteService().doPost(request, response);

		if (response.get(System.RESULT_CODE).equals("-1")) {
			resp.setContentType("text/plain");
			resp.getWriter().println("Error: " + response.get(System.RESULT_MSG));
		} else {
			// registers a new vote event
			Message eventRequest = new Message(request.getContext());
			eventRequest.put(Event.DOCUMENT_TYPE, Vote.RESOURCE_NAME);
			eventRequest.put(Event.TYPE, "POST");
			eventRequest.put(Event.DESCRIPTION, "Vote " + request.get(Vote.VOTE));
			eventRequest.put(Event.OBJECT, response.get(Vote.ID));
			eventRequest.put(Event.USER, ctx.getLoggedInUserProfileId());
			eventRequest.put(Event.TIMESTAMP, request.get(System.TIMESTAMP));

			EventPublisherService pub = new EventPublisherService();
			pub.doPost(eventRequest, response);

			// edit performed, so redirect
			super.redirect(req.getParameter(Question.ID), resp);
		}
	}

	public void __doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Ouch... not implemented yet :(");
	}

	public void __doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Context ctx = (Context) req.getSession().getAttribute(Authtoken.TOKEN);

		Message request = new Message(ctx);
		request.put(Vote.ID, req.getParameter(Vote.ID));
		request.put(Answer.ID, req.getParameter(Answer.ID));
		request.put(Vote.VOTE, req.getParameter(Vote.VOTE));

		Message response = new Message(ctx);

		new VoteService().doPut(request, response);

		if (response.get(System.RESULT_CODE).equals("-1")) {
			resp.setContentType("text/plain");
			resp.getWriter().println("Error: " + response.get(System.RESULT_MSG));
		} else {
			// registers a new vote event
			Message eventRequest = new Message(request.getContext());
			eventRequest.put(Event.DOCUMENT_TYPE, Vote.RESOURCE_NAME);
			eventRequest.put(Event.TYPE, "PUT");
			eventRequest.put(Event.DESCRIPTION, "Vote " + request.get(Vote.VOTE));
			eventRequest.put(Event.OBJECT, request.get(Vote.ID));
			eventRequest.put(Event.USER, ctx.getLoggedInUserProfileId());
			eventRequest.put(Event.TIMESTAMP, request.get(System.TIMESTAMP));

			EventPublisherService pub = new EventPublisherService();
			pub.doPost(eventRequest, response);

			// edit performed, so redirect
			super.redirect(req.getParameter(Question.ID), resp);
		}
	}

	public void __doDelete(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Ouch... not implemented yet :(");
	}
}
