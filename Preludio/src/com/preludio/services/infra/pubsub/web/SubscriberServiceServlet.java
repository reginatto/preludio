package com.preludio.services.infra.pubsub.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.preludio.services.Context;
import com.preludio.services.Message;
import com.preludio.services.infra.pubsub.PubSubMessage;
import com.preludio.services.infra.pubsub.SubscriberService;
import com.preludio.web.BaseServlet;

@SuppressWarnings("serial")
public class SubscriberServiceServlet extends BaseServlet {

	private static final String QUEUE_NAME_HEADER = "X-AppEngine-QueueName";

	@Override
	protected void __doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String queueName = req.getHeader(QUEUE_NAME_HEADER);
		Message request = new Message(new Context());
		request.put(PubSubMessage.QUEUE_NAME, queueName);
		request.put(PubSubMessage.CONTENT, req.getParameter(PubSubMessage.CONTENT));

		new SubscriberService().doPost(request, new Message(new Context()));
	}

	@Override
	protected void __doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void __doPut(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void __doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

}
