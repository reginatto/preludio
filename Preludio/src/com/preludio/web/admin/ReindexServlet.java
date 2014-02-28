package com.preludio.web.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.GetRequest;
import com.google.appengine.api.search.GetResponse;
import com.preludio.data.PMF;
import com.preludio.services.Context;
import com.preludio.services.Message;
import com.preludio.services.infra.data.PersistentDocument;
import com.preludio.services.question.Question;
import com.preludio.services.search.QuestionSearchIndex;

@SuppressWarnings("serial")
public class ReindexServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		Query q = null;

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

			// retrieve all questions
			PersistenceManager pm = PMF.getInstance().getPersistenceManager();
			q = pm.newQuery(PersistentDocument.class);
			q.setFilter("type == typeParam");
			q.declareParameters("String typeParam");
			List<PersistentDocument> results = (List<PersistentDocument>) q.execute("question");
			resp.getWriter().println("Retrieved questions: " + results.size());

			// add them to the index
			int added = 0;
			for (PersistentDocument doc : results) {
				Message docAsMessage = new Message(new Context());
				docAsMessage.fromJson(doc.getContentAsString());
				Document.Builder builder = Document.newBuilder()
			    .addField(Field.newBuilder().setName(Question.ID).setText(docAsMessage.get(Question.ID)))
			    .addField(Field.newBuilder().setName(Question.TITLE).setText(docAsMessage.get(Question.TITLE)))
			    .addField(Field.newBuilder().setName(Question.DETAILS).setHTML(docAsMessage.get(Question.DETAILS)));

				Document d = builder.build();
			
				QuestionSearchIndex.QUESTION_INDEX.put(d);
				added++;
			}
			resp.getWriter().println("Added entries: " + added);

			resp.getWriter().println("Finished");
		} catch (RuntimeException e) {
			resp.getWriter().println("Error: " + e.getMessage());
			e.printStackTrace(resp.getWriter());
		} finally {
			q.closeAll();
		}
	}
}
