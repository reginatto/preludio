package com.preludio.web.admin;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.preludio.data.PMF;
import com.preludio.services.infra.data.PersistentDocument;

@SuppressWarnings("serial")
public class DownloadAnswersServlet extends HttpServlet {

	public static final String FIELD_SEPARATOR = "{{FIELD_SEPARATOR}}";
	public static final String RECORD_SEPARATOR = "{{RECRD_SEPARATOR}}";

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");

		// retrieve all Answer objects
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();

		resp.getWriter().println("Answer Document Objects");
		resp.getWriter().println("");

		Query q = pm.newQuery(PersistentDocument.class);
		q.setFilter("type == typeParam");
		q.declareParameters("String typeParam");
		List<PersistentDocument> questionResults = (List<PersistentDocument>) q.execute("answer");

		int i = 0;
		for (PersistentDocument doc : questionResults) {
			resp.getWriter().println(doc.getContentAsString());
			i++;
			if (i < questionResults.size()) {
				resp.getWriter().println(RECORD_SEPARATOR);
			}
		}

		q.closeAll();
	}
}
