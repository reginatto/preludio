package com.preludio.services.profile;

import com.preludio.services.Message;
import com.preludio.services.Service;
import com.preludio.services.System;

public class ProfileIdService extends Service {

	@Override
	protected String getName() {
		return ProfileId.RESOURCE_NAME;
	}

	@Override
	// TODO Implement this properly
	protected void post(Message request, Message response) {
		String fullName = request.get(Profile.FULL_NAME);
		String profileId = new String();
		if (fullName != null) {
			profileId = fullName;
			profileId = profileId.replaceAll("[?',.]"," ");
			profileId = profileId.trim();
			profileId = profileId.replaceAll(" +","");
		} else {
			profileId = "empty";
		}

		if (response == null) {
			response = new Message(request.getContext());
		}

		response.put(Profile.ID, "/" + Profile.RESOURCE_NAME + "/" + profileId);
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
