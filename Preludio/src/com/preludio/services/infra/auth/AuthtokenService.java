package com.preludio.services.infra.auth;

import javax.jdo.JDOObjectNotFoundException;

import com.preludio.services.Message;
import com.preludio.services.Service;
import com.preludio.services.System;
import com.preludio.services.infra.auth.exception.AuthenticationException;
import com.preludio.services.infra.auth.exception.LoginException;
import com.preludio.services.profile.Profile;

public class AuthtokenService extends Service {

	@Override
	protected String getName() {
		return Authtoken.RESOURCE_NAME;
	}

    @Override
	protected void post(Message request, Message response) {
    	try {
    		// obtain auth info (hash)
    		new AuthenticationInfoService().doGet(request, response);

			if (AuthUtils.validatePassword(request.get(Profile.PASSWORD), response.get(AuthenticationInfo.HASH))) {
				response.put(System.RESULT_CODE, "0");
				response.put(System.RESULT_MSG, "Success");
			} else {
				response.remove(Authtoken.TOKEN);
				throw new LoginException("Authentication failed");
			}
		} catch (JDOObjectNotFoundException e) {
			response.remove(Authtoken.TOKEN);
			throw new LoginException("Authentication failed");
		} catch (AuthenticationException e) {
			response.remove(Authtoken.TOKEN);
			throw new LoginException(e);
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
