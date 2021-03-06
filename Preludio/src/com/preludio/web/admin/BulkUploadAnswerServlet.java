package com.preludio.web.admin;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.preludio.services.Context;
import com.preludio.services.Message;
import com.preludio.services.answer.AnswerService;

@SuppressWarnings("serial")
public class BulkUploadAnswerServlet extends HttpServlet {

	public static final String FIELD_SEPARATOR = "{{FIELD_SEPARATOR}}";
	public static final String RECORD_SEPARATOR = "{{RECRD_SEPARATOR}}";

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");

		String string = req.getParameter("string");
		String[] answers = string.split("\\{\\{RECRD_SEPARATOR\\}\\}");

		AnswerService service = new AnswerService();

		int i = 0;
		for (String answer : answers) {
			answer = answer.trim();
			try {
				Message request = new Message(new Context());
				request.fromJson(answer);
				service.doPost(request, new Message(new Context()));
				resp.getWriter().println("Wrote record " + i);
			} catch (Exception e) {
				resp.getWriter().println("Error writing record " + i);
			}
			i++;
		}
	}
}
