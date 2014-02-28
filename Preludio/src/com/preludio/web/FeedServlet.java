package com.preludio.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.preludio.services.Context;
import com.preludio.services.Message;
import com.preludio.services.System;
import com.preludio.services.infra.auth.Authtoken;
import com.preludio.services.infra.data.Document;
import com.preludio.services.profile.Profile;
import com.preludio.services.question.Question;
import com.preludio.services.question.QuestionService;
import com.preludio.services.search.QuestionSearchIndex;

@SuppressWarnings("serial")
public class FeedServlet extends BaseServlet {

	public static final String FEED_VIEW = "/feed/view";

	public void __doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Ouch... not implemented yet :(");
	}

	public void __doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		// searching for questions
		// for now, get them all
		Context ctx = (Context) req.getSession().getAttribute(Authtoken.TOKEN);
		Message request = new Message(ctx);
		Message response = new Message(ctx);

		new QuestionService().doGet(request, response);
		String results = response.get(QuestionSearchIndex.RESULTS);
		List<Message> questions = new ArrayList<Message>();
		if (!results.equals("")) {
			int intResults = new Integer(results);
			String prefix = new String();
			Message question;
			for (int i = 0; i < intResults; i++) {
				prefix = Question.RESOURCE_NAME + "[" + i + "].";

				question = new Message(ctx);
				question.put(Question.ID, response.get(prefix + Question.ID));
				question.put(Question.TITLE, response.get(prefix + Question.TITLE));
				question.put(Question.DETAILS, response.get(prefix + Question.DETAILS));
				question.put(Question.ANSWERS, response.get(prefix + Question.ANSWERS));
				question.put(Document.AUTHOR, response.get(prefix + Document.AUTHOR));
				question.put(System.TIMESTAMP, response.get(prefix + System.TIMESTAMP));
				question.put(Profile.FULL_NAME, response.get(prefix + Profile.FULL_NAME));
				question.put(Profile.IMAGE_KEY, response.get(prefix + Profile.IMAGE_KEY));

				questions.add(question);
			}
		}

		req.setAttribute(Question.RESOURCE_NAME, questions);

		// no edit performed, so forward
		super.forward(FEED_VIEW, req, resp);
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
