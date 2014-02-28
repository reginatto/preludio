package com.preludio.web.admin;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.preludio.data.PMF;
import com.preludio.services.admin.event.Event;

@SuppressWarnings("serial")
public class EventsReportServlet extends HttpServlet {

	public static final String FIELD_SEPARATOR = "{{FIELD_SEPARATOR}}";
	public static final String RECORD_SEPARATOR = "{{RECRD_SEPARATOR}}";

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String fromStr = req.getParameter("from");
		String toStr = req.getParameter("to");
		String whoStr = req.getParameter("who");
		String whatStr = req.getParameter("what");
		String objectStr = req.getParameter("object");

		// transform dates to timestamps
		Long from = -1L;
		try {
			Date fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromStr);
			from = fromDate.getTime();
		} catch (ParseException e) {
		}

		Long to = -1L;
		try {
			Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(toStr);
			to = toDate.getTime();
		} catch (ParseException e) {
		}

		// search for events based on criteria
		StringBuffer filter = new StringBuffer();
		StringBuffer paramDeclaration = new StringBuffer();
		List<Object> params = new ArrayList<Object>();

		if (from != -1) {
			// add 'from' parameter to query
			this.addToStringBuffer(filter, "timestamp >= fromParam", "&&");
			this.addToStringBuffer(paramDeclaration, "Long fromParam", ",");
			params.add(from);
		}

		if (to != -1) {
			// add 'to' parameter to query
			this.addToStringBuffer(filter, "timestamp <= toParam", "&&");
			this.addToStringBuffer(paramDeclaration, "Long toParam", ",");
			params.add(to);
		}

		if ((whoStr != null) && (!whoStr.equals(""))) {
			// add 'who' parameter to query
			this.addToStringBuffer(filter, "user == whoParam", "&&");
			this.addToStringBuffer(paramDeclaration, "String whoParam", ",");
			params.add(whoStr);
		}

		if ((whatStr != null) && (!whatStr.equals(""))) {
			String[] types = whatStr.split(":");
			if (types.length == 1) {
				this.addToStringBuffer(filter, "(docType == docTypeParam)", "&&");
				this.addToStringBuffer(paramDeclaration, "String docTypeParam", ",");
				params.add(types[0]);
			} else if (types.length == 2) {
				this.addToStringBuffer(filter, "(docType == docTypeParam && type == typeParam)", "&&");
				this.addToStringBuffer(paramDeclaration, "String docTypeParam", ",");
				this.addToStringBuffer(paramDeclaration, "String typeParam", ",");
				params.add(types[0]);
				params.add(types[1]);
			}
		}

		if ((objectStr != null) && (!objectStr.equals(""))) {
			// add 'object' parameter to query
			this.addToStringBuffer(filter, "object == objectParam", "&&");
			this.addToStringBuffer(paramDeclaration, "String objectParam", ",");
			params.add(objectStr);
		}

		PersistenceManager pm = PMF.getInstance().getPersistenceManager();

		Query q = pm.newQuery(Event.class);
		if (filter.length() > 0) {
			q.setFilter(filter.toString());
		}
		if (paramDeclaration.length() > 0) {
			q.declareParameters(paramDeclaration.toString());
		}

		// order by timestamp from older to newer
		q.setOrdering("timestamp asc");

		try {
			List<Event> events = (List<Event>) q.executeWithArray(params.toArray());
			req.setAttribute("events", events);

			// forward
			req.getRequestDispatcher("/admin/events/report/view").forward(req, resp);
		} finally {
			q.closeAll();
		}
	}

	private void addToStringBuffer(StringBuffer buffer, String content, String separator) {
		if (buffer.length() > 0) {
			buffer.append(" ").append(separator).append(" ").append(content);
		} else {
			// first insertion
			buffer.append(content);
		}
	}
}
