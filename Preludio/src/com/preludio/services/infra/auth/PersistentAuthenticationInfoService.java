package com.preludio.services.infra.auth;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import com.preludio.data.PMF;
import com.preludio.services.Message;
import com.preludio.services.Service;
import com.preludio.services.System;
import com.preludio.services.profile.Profile;

public class PersistentAuthenticationInfoService extends Service {

	@Override
	protected String getName() {
		return "persistent" + AuthenticationInfo.RESOURCE_NAME;
	}

	@Override
	protected void post(Message request, Message response) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();

			AuthenticationInfo a = new AuthenticationInfo();
			a.setEmail(request.get(Profile.EMAIL));
			a.setProfileId(request.get(Profile.ID));
			a.setHash(request.get(AuthenticationInfo.HASH));

			pm.makePersistent(a);

			tx.commit();

			if (response == null) {
				response = new Message(request.getContext());
			}

			response.put(System.RESULT_CODE, "0");
			response.put(System.RESULT_MSG, "Success");
		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
			}

			if (response == null) {
				response = new Message(request.getContext());
			}

			response.put(System.RESULT_CODE, "-1");
			response.put(System.RESULT_MSG, "Error accessing the data store: " + e.getMessage());
		} finally {
			pm.close();
		}
	}

	@Override
	protected void get(Message request, Message response) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		AuthenticationInfo a = pm.getObjectById(AuthenticationInfo.class, request.get(Profile.EMAIL));

		if (response == null) {
			response = new Message(request.getContext());
		}

		response.put(AuthenticationInfo.HASH, a.getHash());
		response.put(Profile.ID, a.getProfileId());
		response.put(Authtoken.TOKEN, a.getProfileId());

		response.put(System.RESULT_CODE, "0");
		response.put(System.RESULT_MSG, "Success");
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
