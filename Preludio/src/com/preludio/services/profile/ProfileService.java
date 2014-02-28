package com.preludio.services.profile;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.preludio.services.Message;
import com.preludio.services.Service;
import com.preludio.services.System;
import com.preludio.services.infra.data.Document;
import com.preludio.services.infra.data.PersistentDocumentService;

public class ProfileService extends Service {

	@Override
	protected String getName() {
		return Profile.RESOURCE_NAME;
	}

	@Override
	protected void post(Message request, Message response) {
		// generates profile id
		new ProfileIdService().doPost(request, response);
		request.put(Profile.ID, response.get(Profile.ID));

		// create profile
		request.put(Document.ID, response.get(Profile.ID));
		request.put(Document.TYPE, Profile.RESOURCE_NAME);
		request.put(Profile.POINTS, "" + Profile.INITIAL_POINTS);
		request.remove(Profile.PASSWORD);
		new PersistentDocumentService().doPost(request, response);
	}

	@Override
	protected void get(Message request, Message response) {
		if (request.containsField(Profile.ID)) {
			// retrieve profile based on id
			request.put(Document.ID, request.get(Profile.ID));
			new PersistentDocumentService().doGet(request, response);
		} else {
			// search
			if (response == null) {
				response = new Message(request.getContext());
			}

			response.put(System.RESULT_CODE, "-1");
			response.put(System.RESULT_MSG, "Not implemented");
		}
	}

	@Override
	protected void put(Message request, Message response) {
		// retrieve profile based on id
		request.put(Document.ID, request.get(Profile.ID));
		Message origProfile = new Message(request.getContext());
		new PersistentDocumentService().doGet(request, origProfile);

		// sets update fields
		Message updateReq = new Message(request.getContext());
		updateReq.putAllFields(origProfile.getFields());
		updateReq.putAllReferences(origProfile.getReferences());

		// update profile fields
		if (request.containsField(Profile.DESCRIPTION)) {
			updateReq.put(Profile.DESCRIPTION, request.get(Profile.DESCRIPTION));
		}
		if (request.containsField(Profile.LOCATION)) {
			updateReq.put(Profile.LOCATION, request.get(Profile.LOCATION));
		}
		if (request.containsField(Profile.IMAGE_KEY)) {
			updateReq.put(Profile.IMAGE_KEY, request.get(Profile.IMAGE_KEY));
		}

		if (request.containsField(Profile.POINTS)) {
			int currentPoints = new Integer(origProfile.get(Profile.POINTS));
			if (!request.get(Profile.POINTS).equals("")) {
				currentPoints += new Integer(request.get(Profile.POINTS));
				updateReq.put(Profile.POINTS, "" + currentPoints);
			}
		}

		// if picture has changed, remove previous one
		if (request.containsField(Profile.IMAGE_KEY)) {
			if (!request.get(Profile.IMAGE_KEY).equals(origProfile.get(Profile.IMAGE_KEY))) {
				BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
				try {
					blobstoreService.delete(new BlobKey(origProfile.get(Profile.IMAGE_KEY)));
				} catch (Exception e) {
				}
			}
		}

		// stores document
		new PersistentDocumentService().doPut(updateReq, response);
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
