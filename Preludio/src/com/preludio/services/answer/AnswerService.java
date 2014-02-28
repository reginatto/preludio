package com.preludio.services.answer;

import com.preludio.services.Message;
import com.preludio.services.Service;
import com.preludio.services.System;
import com.preludio.services.answer.vote.Vote;
import com.preludio.services.infra.data.Document;
import com.preludio.services.infra.data.PersistentDocumentService;
import com.preludio.services.profile.Profile;
import com.preludio.services.profile.ProfileService;
import com.preludio.services.question.Question;
import com.preludio.services.question.QuestionService;

public class AnswerService extends Service {

	@Override
	protected String getName() {
		return Answer.RESOURCE_NAME;
	}

	@Override
	protected void post(Message request, Message response) {
		// generates answer id
		new AnswerIdService().doPost(request, response);

		// sets document fields
		request.put(Answer.ID, response.get(Answer.ID));
		request.put(Document.ID, response.get(Answer.ID));
		request.put(Document.TYPE, Answer.RESOURCE_NAME);

		// stores document
		new PersistentDocumentService().doPost(request, response);

		// adds reference to question and updates question
		request.putReference(Question.ANSWERS, Answer.ID, response.get(Answer.ID));
		new QuestionService().doPut(request, response);

		// updates user points
		request.put(Profile.ID, request.get(Document.AUTHOR));
		request.put(Profile.POINTS, "" + Answer.POINTS_FOR_ANSWER);
		new ProfileService().doPut(request, response);
	}

	@Override
	protected void get(Message request, Message response) {
		if (request.containsField(Answer.ID)) {
			// retrieve answer based on id
			request.put(Document.ID, request.get(Answer.ID));
			new PersistentDocumentService().doGet(request, response);
		} else {
			// search not implemented
		}
	}

	@Override
	protected void put(Message request, Message response) {
		// retrieve answer based on id
		request.put(Document.ID, request.get(Answer.ID));
		Message origAnswer = new Message(request.getContext());
		new PersistentDocumentService().doGet(request, origAnswer);

		// sets update fields
		Message updateReq = new Message(request.getContext());
		updateReq.putAllFields(origAnswer.getFields());
		updateReq.putAllReferences(origAnswer.getReferences());

		// calculate new # of votes
		int votes = new Integer(origAnswer.get(Answer.VOTES));
		if (request.get(Vote.VOTE).equals(Vote.VOTE_UP)) {
			votes++;
		} else if (request.get(Vote.VOTE).equals(Vote.VOTE_DOWN)) {
			if (votes > 0) {
				votes--;
			}
		}

		updateReq.put(Answer.VOTES, "" + votes);

		updateReq.putAllReferences(request.getReferences());

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
