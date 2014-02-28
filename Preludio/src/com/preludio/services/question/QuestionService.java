package com.preludio.services.question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.preludio.services.Message;
import com.preludio.services.Service;
import com.preludio.services.System;
import com.preludio.services.infra.data.Document;
import com.preludio.services.infra.data.PersistentDocumentService;
import com.preludio.services.profile.Profile;
import com.preludio.services.profile.ProfileService;
import com.preludio.services.search.QuestionSearchIndexService;

public class QuestionService extends Service {

	@Override
	protected String getName() {
		return Question.RESOURCE_NAME;
	}

	@Override
	protected void post(Message request, Message response) {
		// generates question id
		new QuestionIdService().doPost(request, response);

		// sets document fields
		String questionId = response.get(Question.ID);
		request.put(Question.ID, questionId);
		request.put(Document.ID, response.get(Question.ID));
		request.put(Document.TYPE, Question.RESOURCE_NAME);

		// stores document
		new PersistentDocumentService().doPost(request, response);

		// adds data to search index
		new QuestionSearchIndexService().doPost(request, response);

		// updates user points
		request.put(Profile.ID, request.get(Document.AUTHOR));
		request.put(Profile.POINTS, "" + Question.POINTS_FOR_QUESTION);
		new ProfileService().doPut(request, response);

		response.put(Question.ID, questionId);
	}

	@Override
	protected void get(Message request, Message response) {
		if (request.containsField(Question.ID)) {
			// retrieve question based on id
			request.put(Document.ID, request.get(Question.ID));
			new PersistentDocumentService().doGet(request, response);
		} else {
			new QuestionSearchIndexService().doGet(request, response);
		}
	}

	@Override
	protected void put(Message request, Message response) {
		// retrieve question based on id
		request.put(Document.ID, request.get(Question.ID));
		Message origQuestion = new Message(request.getContext());
		new PersistentDocumentService().doGet(request, origQuestion);

		// sets update fields
		Message updateReq = new Message(request.getContext());
		updateReq.putAllFields(origQuestion.getFields());
		updateReq.putAllReferences(origQuestion.getReferences());

		// update question fields
		if (request.containsCollection(Question.ANSWERS)) {
			Map<String, List<Map<String, String>>> newAnswers  = new HashMap<String, List<Map<String, String>>>();
			newAnswers.put(Question.ANSWERS, request.getReferences(Question.ANSWERS));
			updateReq.putAllReferences(newAnswers);
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
