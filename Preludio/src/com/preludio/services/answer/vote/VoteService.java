package com.preludio.services.answer.vote;

import com.preludio.services.Message;
import com.preludio.services.Service;
import com.preludio.services.System;
import com.preludio.services.answer.AnswerService;
import com.preludio.services.infra.data.Document;
import com.preludio.services.infra.data.PersistentDocumentService;
import com.preludio.services.profile.Profile;
import com.preludio.services.profile.ProfileService;

public class VoteService extends Service {

	@Override
	protected String getName() {
		return Vote.RESOURCE_NAME;
	}

	@Override
	protected void post(Message request, Message response) {
		// generates answer id
		new VoteIdService().doPost(request, response);

		// sets document fields
		request.put(Vote.ID, response.get(Vote.ID));
		request.put(Document.ID, response.get(Vote.ID));
		request.put(Document.TYPE, Vote.RESOURCE_NAME);

		// stores document
		new PersistentDocumentService().doPost(request, response);

		// update the number of votes in the answer
		new AnswerService().doPut(request, response);

		// updates user points
		if (request.get(Vote.VOTE).equals(Vote.VOTE_UP)) {
			Message answerResponse = new Message(request.getContext());
			new AnswerService().doGet(request, answerResponse);
			request.put(Profile.ID, answerResponse.get(Document.AUTHOR));
			request.put(Profile.POINTS, "" + Vote.POINTS_FOR_VOTE_UP);
			new ProfileService().doPut(request, response);
		}
	}

	@Override
	protected void get(Message request, Message response) {
		if (request.containsField(Vote.ID)) {
			// retrieve vote based on id
			request.put(Document.ID, request.get(Vote.ID));
			new PersistentDocumentService().doGet(request, response);
		} else {
			// attempt at finding a vote for a pair profile / answer
			Message idResponse = new Message(request.getContext());
			new VoteIdService().doPost(request, idResponse);
			request.put(Document.ID, idResponse.get(Vote.ID));
			try {
				new PersistentDocumentService().doGet(request, response);
			} catch (Exception e) {
				// no vote found for this profile / answer
			}
		}
	}

	@Override
	protected void put(Message request, Message response) {
		// retrieve vote based on id
		request.put(Document.ID, request.get(Vote.ID));
		Message origVote = new Message(request.getContext());
		new PersistentDocumentService().doGet(request, origVote);

		// sets update fields
		Message updateReq = new Message(request.getContext());
		updateReq.putAllFields(origVote.getFields());
		updateReq.putAllReferences(origVote.getReferences());

		// update the vote value
		updateReq.put(Vote.VOTE, request.get(Vote.VOTE));

		// stores document
		new PersistentDocumentService().doPut(updateReq, response);

		// update the number of votes in the answer
		new AnswerService().doPut(request, response);

		// updates user points
		if (request.get(Vote.VOTE).equals(Vote.VOTE_UP)) {
			Message answerResponse = new Message(request.getContext());
			new AnswerService().doGet(request, answerResponse);
			request.put(Profile.ID, answerResponse.get(Document.AUTHOR));
			request.put(Profile.POINTS, "" + Vote.POINTS_FOR_VOTE_UP);
			new ProfileService().doPut(request, response);
		}
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
