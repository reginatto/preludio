package com.preludio.services.infra.auth;

import com.preludio.services.Message;
import com.preludio.services.Service;
import com.preludio.services.System;
import com.preludio.services.profile.Profile;

public class HashAndSaltService extends Service {

	@Override
	protected String getName() {
		return HashAndSalt.RESOURCE_NAME;
	}

	@Override
	protected void post(Message request, Message response) {
		try {
			String hash = AuthUtils.createHash(request.get(Profile.PASSWORD));
			response.put(AuthenticationInfo.HASH, hash);
			response.put(System.RESULT_CODE, "0");
			response.put(System.RESULT_MSG, "Success");
		} catch (Exception e) {
			response.put(System.RESULT_CODE, "-1");
			response.put(System.RESULT_MSG, "Error: " + e.getMessage());
		}
	}

    @Override
    protected void delete(Message request, Message response) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void get(Message request, Message response) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void put(Message request, Message response) {
		// TODO Auto-generated method stub
	}

}
