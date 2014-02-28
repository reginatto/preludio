package com.preludio.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.gson.Gson;
import com.preludio.services.question.Question;
import com.preludio.services.search.QuestionSearchIndex;

@SuppressWarnings("serial")
public class QuestionSearchTypeaheadServlet extends BaseServlet {

	public void __doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Ouch... not implemented yet :(");
	}

	public void __doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		resp.setContentType("application/json");

		String queryString = req.getParameter(QuestionSearchIndex.QUERY_STRING);
		if (queryString != null) {
			queryString = queryString.trim();
		}

		Query query = Query.newBuilder()
			.setOptions(QueryOptions.newBuilder()
				.setLimit(10)
				.build())
			.build(queryString);

		Results<ScoredDocument> results = QuestionSearchIndex.QUESTION_INDEX.search(query);

		String[] questionTitles = null;
		if (results != null) {
			questionTitles = new String[(int) results.getNumberFound()];
			int i = 0;
			for (ScoredDocument doc : results) {
				questionTitles[i] = doc.getOnlyField(Question.TITLE).getText();
				i++;
			}
		} else {
			questionTitles = new String[0];
		}

		resp.getWriter().write(new Gson().toJson(questionTitles));
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
