package com.preludio.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.preludio.services.Context;
import com.preludio.services.Message;
import com.preludio.services.System;
import com.preludio.services.admin.event.Event;
import com.preludio.services.admin.event.EventPublisherService;
import com.preludio.services.answer.Answer;
import com.preludio.services.answer.AnswerService;
import com.preludio.services.answer.vote.Vote;
import com.preludio.services.answer.vote.VoteService;
import com.preludio.services.infra.auth.Authtoken;
import com.preludio.services.infra.data.Document;
import com.preludio.services.profile.Profile;
import com.preludio.services.profile.ProfileService;
import com.preludio.services.question.Question;
import com.preludio.services.question.QuestionService;
import com.preludio.services.search.QuestionSearchIndex;
import com.preludio.services.topic.Topic;

@SuppressWarnings("serial")
public class QuestionServlet extends BaseServlet {

	public static final String QUESTION_SEARCH_VIEW = "/question/search/view";
	public static final String QUESTION_VIEW = "/question/view";

	public void __doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Context ctx = (Context) req.getSession().getAttribute(Authtoken.TOKEN);

		Message request = new Message(ctx);
		request.put(Document.AUTHOR, ctx.getLoggedInUserProfileId());
		request.put(Topic.NAME, req.getParameter(Topic.NAME));
		request.put(Question.TITLE, req.getParameter(Question.TITLE));
		request.put(Question.DETAILS, req.getParameter(Question.DETAILS));

		Message response = new Message(ctx);

		new QuestionService().doPost(request, response);

		if (response.get(System.RESULT_CODE).equals("-1")) {
			resp.setContentType("text/plain");
			resp.getWriter().println("Error: " + response.get(System.RESULT_MSG));
		} else {
			// registers a new question event
			Message eventRequest = new Message(request.getContext());
			eventRequest.put(Event.DOCUMENT_TYPE, Question.RESOURCE_NAME);
			eventRequest.put(Event.TYPE, "POST");
			eventRequest.put(Event.DESCRIPTION, "New Question");
			eventRequest.put(Event.OBJECT, response.get(Question.ID));
			eventRequest.put(Event.USER, ctx.getLoggedInUserProfileId());
			eventRequest.put(Event.TIMESTAMP, request.get(System.TIMESTAMP));

			EventPublisherService pub = new EventPublisherService();
			pub.doPost(eventRequest, response);

			// edit performed, so redirect
			super.redirect(response.get(Question.ID), resp);
		}
	}

	public void __doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		Context ctx = (Context) req.getSession().getAttribute(Authtoken.TOKEN);
		if (ctx == null) {
			ctx = new Context();
		}

		String questionId = new String();
		if (req.getPathInfo() != null) {
			questionId = "/" + Question.RESOURCE_NAME + "/" + req.getPathInfo().replaceAll("/", "");
		}

		if (!questionId.equals("")) {
			// retrieving specific question
			Message request = new Message(ctx);
			request.put(Question.ID, questionId);

			Message response = new Message(ctx);

			// retrieve question
			new QuestionService().doGet(request, response);

			// retrieve author's name
			Message profileReq = new Message(ctx);
			Message profileResp = new Message(ctx);
			profileReq.put(Profile.ID, response.get(Document.AUTHOR));
			new ProfileService().doGet(profileReq, profileResp);
			response.put(Profile.FULL_NAME, profileResp.get(Profile.FULL_NAME));

			req.setAttribute(Question.RESOURCE_NAME, response);

			// retrieve answers
			List<Map<String, String>> answers = new ArrayList<Map<String,String>>();
			Message answerResp, voteResp;
			for (Map<String, String> ref : response.getReferences(Question.ANSWERS)) {
				request = new Message(ctx);
				request.put(Answer.ID, ref.get(Answer.ID));
				answerResp = new Message(ctx);

				new AnswerService().doGet(request, answerResp);

				Map<String, String> answer = new HashMap<String, String>();
				answer.put(Answer.ID, answerResp.get(Answer.ID));
				answer.put(Answer.CONTENT, answerResp.get(Answer.CONTENT));
				answer.put(Answer.VOTES, answerResp.get(Answer.VOTES));
				answer.put(Document.AUTHOR, answerResp.get(Document.AUTHOR));
				answer.put(System.TIMESTAMP, answerResp.get(System.TIMESTAMP));

				// retrieve author's name
				profileReq = new Message(ctx);
				profileResp = new Message(ctx);
				profileReq.put(Profile.ID, answerResp.get(Document.AUTHOR));
				new ProfileService().doGet(profileReq, profileResp);
				answer.put(Profile.FULL_NAME, profileResp.get(Profile.FULL_NAME));

				// retrieve this user's vote
				if (ctx != null) {
					request = new Message(ctx);
					request.put(Profile.ID, ctx.getLoggedInUserProfileId());
					request.put(Answer.ID, ref.get(Answer.ID));
					voteResp = new Message(ctx);

					new VoteService().doGet(request, voteResp);

					if (voteResp.containsField(Vote.ID)) {
						answer.put(Vote.ID, voteResp.get(Vote.ID));
						answer.put(Vote.VOTE, voteResp.get(Vote.VOTE));
					}
				}

				answers.add(answer);
			}
			req.setAttribute(Answer.RESOURCE_NAME, answers);

			// registers a question view event
			Message eventRequest = new Message(request.getContext());
			eventRequest.put(Event.DOCUMENT_TYPE, Question.RESOURCE_NAME);
			eventRequest.put(Event.TYPE, "GET");
			eventRequest.put(Event.DESCRIPTION, "View Question");
			eventRequest.put(Event.OBJECT, questionId);
			eventRequest.put(Event.USER, ctx.getLoggedInUserProfileId());
			eventRequest.put(Event.TIMESTAMP, request.get(System.TIMESTAMP));

			EventPublisherService pub = new EventPublisherService();
			pub.doPost(eventRequest, response);

			// no edit performed, so forward
			super.forward(QUESTION_VIEW, req, resp);
		} else {
			// searching for questions
			Message request = new Message(ctx);
			if (req.getParameter(QuestionSearchIndex.QUERY_STRING) != null) {
				request.put(QuestionSearchIndex.QUERY_STRING, req.getParameter(QuestionSearchIndex.QUERY_STRING));
			} else {
				request.put(QuestionSearchIndex.QUERY_STRING, "");
			}

			Message response = new Message(ctx);

			new QuestionService().doGet(request, response);

			response.put(QuestionSearchIndex.QUERY_STRING, request.get(QuestionSearchIndex.QUERY_STRING));
			req.setAttribute(Question.RESOURCE_NAME, response);

			// registers a question search event
			Message eventRequest = new Message(request.getContext());
			eventRequest.put(Event.DOCUMENT_TYPE, QuestionSearchIndex.RESOURCE_NAME);
			eventRequest.put(Event.TYPE, "GET");
			eventRequest.put(Event.DESCRIPTION, "Search Question");
			eventRequest.put(Event.OBJECT, request.get(QuestionSearchIndex.QUERY_STRING));
			eventRequest.put(Event.USER, ctx.getLoggedInUserProfileId());
			eventRequest.put(Event.TIMESTAMP, request.get(System.TIMESTAMP));

			EventPublisherService pub = new EventPublisherService();
			pub.doPost(eventRequest, response);

			// no edit performed, so forward
			super.forward(QUESTION_SEARCH_VIEW, req, resp);
		}
	}

	public void __doPut(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Ouch... not implemented yet :(");
	}

	public void __doDelete(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Ouch... not implemented yet :(");
	}
}
