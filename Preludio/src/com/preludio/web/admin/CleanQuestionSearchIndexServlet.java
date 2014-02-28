package com.preludio.web.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.GetRequest;
import com.google.appengine.api.search.GetResponse;
import com.preludio.services.search.QuestionSearchIndex;

@SuppressWarnings("serial")
public class CleanQuestionSearchIndexServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");

		try {
			// delete index entries
			int entries = 0;
			while (true) {
				List<String> docIds = new ArrayList<String>();
				// Return a set of document IDs.
				GetRequest request = GetRequest.newBuilder().setReturningIdsOnly(
						true).build();
				GetResponse<Document> response = QuestionSearchIndex.QUESTION_INDEX.getRange(request);
				if (response.getResults().isEmpty()) {
					break;
				}
				for (Document doc : response) {
					docIds.add(doc.getId());
				}
				QuestionSearchIndex.QUESTION_INDEX.delete(docIds);
				entries++;
			}
			resp.getWriter().println("Deleted entries: " + entries);
		} catch (RuntimeException e) {
			resp.getWriter().println("Error: " + e.getMessage());
			e.printStackTrace(resp.getWriter());
		}
	}
}
