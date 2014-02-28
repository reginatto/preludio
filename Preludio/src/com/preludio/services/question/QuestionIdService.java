package com.preludio.services.question;

import com.preludio.services.Message;
import com.preludio.services.Service;
import com.preludio.services.System;

public class QuestionIdService extends Service {

	@Override
	protected String getName() {
		return QuestionId.RESOURCE_NAME;
	}

	@Override
	// TODO Implement this properly
	protected void post(Message request, Message response) {
		String originalName = request.get(Question.TITLE);
		String safeName = new String();
		if (originalName != null) {
			safeName = originalName;
			safeName = safeName.replaceAll("[?',.]"," ");
			safeName = safeName.trim();
			safeName = safeName.replaceAll(" +","-");
		} else {
			safeName = "empty";
		}

		if (response == null) {
			response = new Message(request.getContext());
		}

		response.put(Question.ID, "/" + Question.RESOURCE_NAME + "/" + safeName);
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
