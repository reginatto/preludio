package com.preludio.web.admin;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.preludio.data.PMF;
import com.preludio.services.infra.auth.AuthenticationInfo;
import com.preludio.services.infra.data.PersistentDocument;

@SuppressWarnings("serial")
public class DownloadProfilesServlet extends HttpServlet {

	public static final String FIELD_SEPARATOR = "{{FIELD_SEPARATOR}}";
	public static final String RECORD_SEPARATOR = "{{RECRD_SEPARATOR}}";

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");

		// retrieve all AuthenticationInfo objects
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Query q = pm.newQuery(AuthenticationInfo.class);
		List<AuthenticationInfo> authResults = (List<AuthenticationInfo>) q.execute("");

		resp.getWriter().println("Authinfo Objects");
		resp.getWriter().println("");

		int i = 0;
		for (AuthenticationInfo auth : authResults) {
			resp.getWriter().println(auth.getEmail() + FIELD_SEPARATOR
					+ auth.getHash() + FIELD_SEPARATOR
					+ auth.getProfileId());
			i++;
			if (i < authResults.size()) {
				resp.getWriter().println(RECORD_SEPARATOR);
			}
		}

		resp.getWriter().println("Profile Document Objects");
		resp.getWriter().println("");

		q = pm.newQuery(PersistentDocument.class);
		q.setFilter("type == typeParam");
		q.declareParameters("String typeParam");
		List<PersistentDocument> profileResults = (List<PersistentDocument>) q.execute("profile");

		i = 0;
		for (PersistentDocument doc : profileResults) {
			resp.getWriter().println(doc.getContentAsString());
			i++;
			if (i < profileResults.size()) {
				resp.getWriter().println(RECORD_SEPARATOR);
			}
		}

		q.closeAll();
	}
}
