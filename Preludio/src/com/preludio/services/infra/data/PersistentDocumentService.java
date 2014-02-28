package com.preludio.services.infra.data;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import com.preludio.data.PMF;
import com.preludio.services.Message;
import com.preludio.services.Service;
import com.preludio.services.System;

public class PersistentDocumentService extends Service {

	@Override
	protected String getName() {
		return PersistentDocument.RESOURCE_NAME;
	}

	@Override
	protected void post(Message request, Message response) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();

			PersistentDocument doc = new PersistentDocument();
			doc.setId(request.get(Document.ID));
			doc.setType(request.get(Document.TYPE));
			doc.setUserId(request.getContext().getLoggedInUserProfileId());
			doc.setContentString(request.toJson());

			pm.makePersistent(doc);

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
		if (request.containsField(Document.ID)) {
			// retrieving specific question
			PersistentDocument doc = pm.getObjectById(PersistentDocument.class, request.get(Document.ID));

			if (response == null) {
				response = new Message(request.getContext());
			}

			response.fromJson(doc.getContentAsString());
			response.setContext(request.getContext());

//			response.put(System.RESULT_CODE, "0");
//			response.put(System.RESULT_MSG, "Success");
		} else {
			// search not implemented
		}
	}

	@Override
	protected void delete(Message request, Message response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void put(Message request, Message response) {
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		try {
			PersistentDocument doc = pm.getObjectById(PersistentDocument.class, request.get(Document.ID));
			doc.setContentString(request.toJson());

			if (response == null) {
				response = new Message(request.getContext());
			}

			response.put(System.RESULT_CODE, "0");
			response.put(System.RESULT_MSG, "Success");
		} catch (Exception e) {
			if (response == null) {
				response = new Message(request.getContext());
			}

			response.put(System.RESULT_CODE, "-1");
			response.put(System.RESULT_MSG, "Error accessing the data store: " + e.getMessage());
		} finally {
			pm.close();
		}
	}

}
