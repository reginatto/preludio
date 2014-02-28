package com.preludio.services.answer.vote;

import com.preludio.services.Message;
import com.preludio.services.Service;
import com.preludio.services.System;
import com.preludio.services.answer.Answer;
import com.preludio.services.profile.Profile;

public class VoteIdService extends Service {

	@Override
	protected String getName() {
		return VoteId.RESOURCE_NAME;
	}

	@Override
	protected void post(Message request, Message response) {
		// vote id is in the format:
		// /profile/some-profile#/answer/some-answer
		response.put(Vote.ID, request.get(Profile.ID) + "#" + request.get(Answer.ID));
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
