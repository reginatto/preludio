package com.preludio.services.admin.event;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import com.preludio.data.PMF;
import com.preludio.services.Message;
import com.preludio.services.Service;
import com.preludio.services.System;

public class EventService extends Service {

	@Override
	protected String getName() {
		return Event.RESOURCE_NAME;
	}

	@Override
	protected void post(Message request, Message response) {
		// obtain id
		new EventIdService().doPost(request, response);

		// stores event
		PersistenceManager pm = PMF.getInstance().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();

			Event event = new Event();
			event.setId(response.get(Event.ID));
			event.setDocType(request.get(Event.DOCUMENT_TYPE));
			event.setType(request.get(Event.TYPE));
			event.setDescription(request.get(Event.DESCRIPTION));
			event.setObject(request.get(Event.OBJECT));
			event.setUser(request.get(Event.USER));
			event.setTimestamp(new Long(request.get(Event.TIMESTAMP)));

			pm.makePersistent(event);

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
		if (request.containsField(Event.ID)) {
			// retrieving specific question
			Event event = pm.getObjectById(Event.class, request.get(Event.ID));

			if (response == null) {
				response = new Message(request.getContext());
			}

			response.put(Event.ID, event.getId());
			response.put(Event.DOCUMENT_TYPE, event.getDocType());
			response.put(Event.TYPE, event.getType());
			response.put(Event.DESCRIPTION, event.getDescription());
			response.put(Event.OBJECT, event.getObject());
			response.put(Event.USER, event.getUser());
			response.put(Event.TIMESTAMP, "" + event.getTimestamp());

			response.setContext(request.getContext());

			response.put(System.RESULT_CODE, "0");
			response.put(System.RESULT_MSG, "Success");
		} else {
			// search not yet implemented
			// TODO should be based on timestamp intervals
		}
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
