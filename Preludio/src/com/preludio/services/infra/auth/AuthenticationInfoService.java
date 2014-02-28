package com.preludio.services.infra.auth;

import com.preludio.services.Message;
import com.preludio.services.Service;

public class AuthenticationInfoService extends Service {

	@Override
	protected String getName() {
		return AuthenticationInfo.RESOURCE_NAME;
	}

	@Override
	protected void post(Message request, Message response) {
		// hash password
		new HashAndSaltService().doPost(request, response);
		request.put(AuthenticationInfo.HASH, response.get(AuthenticationInfo.HASH));

		// creates an entry on the authentication database
		new PersistentAuthenticationInfoService().doPost(request, response);
	}

	@Override
	protected void get(Message request, Message response) {
		// obtain auth info (hash)
		new PersistentAuthenticationInfoService().doGet(request, response);
	}

	@Override
	protected void delete(Message request, Message response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void put(Message request, Message response) {
		// TODO Auto-generated method stub
		
	}

}
