package com.preludio.services.answer;

import java.util.UUID;

import com.preludio.services.Message;
import com.preludio.services.Service;
import com.preludio.services.System;

public class AnswerIdService extends Service {

	@Override
	protected String getName() {
		return AnswerId.RESOURCE_NAME;
	}

	@Override
	protected void post(Message request, Message response) {
		String id = UUID.randomUUID().toString();
		response.put(Answer.ID, "/" + Answer.RESOURCE_NAME + "/" + id);
	}

	@Override
	protected void get(Message request, Message response) {
		// TODO Auto-generated method stub
		if (response == null) {
			response = new Message(request.getContext());
		}

		response.put(System.RESULT_CODE, "-1");
		response.put(System.RESULT_MSG, "Not implemented");
	}

	@Override
	protected void put(Message request, Message response) {
		// TODO Auto-generated method stub
		if (response == null) {
			response = new Message(request.getContext());
		}

		response.put(System.RESULT_CODE, "-1");
		response.put(System.RESULT_MSG, "Not implemented");
	}

	@Override
	protected void delete(Message request, Message response) {
		// TODO Auto-generated method stub
		if (response == null) {
			response = new Message(request.getContext());
		}

		response.put(System.RESULT_CODE, "-1");
		response.put(System.RESULT_MSG, "Not implemented");
	}

}
