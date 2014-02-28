package com.preludio.web.admin;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.preludio.data.PMF;
import com.preludio.services.infra.auth.AuthenticationInfo;

@SuppressWarnings("serial")
public class BulkUploadAuthinfoServlet extends HttpServlet {

	public static final String FIELD_SEPARATOR = "{{FIELD_SEPARATOR}}";
	public static final String RECORD_SEPARATOR = "{{RECRD_SEPARATOR}}";

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");

		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		String string = req.getParameter("string");
		String[] authInfos = string.split("\\{\\{RECRD_SEPARATOR\\}\\}");

		int i = 0;
		for (String authInfo : authInfos) {
			authInfo = authInfo.trim();
			try {
				tx.begin();

				String[] fields = authInfo.split("\\{\\{FIELD_SEPARATOR\\}\\}");

				AuthenticationInfo a = new AuthenticationInfo();
				a.setEmail(fields[0]);
				a.setHash(fields[1]);
				a.setProfileId(fields[2]);

				pm.makePersistent(a);

				tx.commit();

				resp.getWriter().println("Wrote record for " + fields[0]);
			} catch (Exception e) {
				if (tx.isActive()) {
					tx.rollback();
				}

				resp.getWriter().println("Error writing record " + i);
			}
			i++;
		}

		pm.close();

	}
}
