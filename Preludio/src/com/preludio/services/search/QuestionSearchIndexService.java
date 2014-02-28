package com.preludio.services.search;

import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.preludio.services.Message;
import com.preludio.services.Service;
import com.preludio.services.System;
import com.preludio.services.infra.data.Document;
import com.preludio.services.profile.Profile;
import com.preludio.services.profile.ProfileService;
import com.preludio.services.question.Question;
import com.preludio.services.question.QuestionService;

public class QuestionSearchIndexService extends Service {

	@Override
	protected String getName() {
		return QuestionSearchIndex.RESOURCE_NAME;
	}

	@Override
	protected void post(Message request, Message response) {
		// add question id, title and details to search
		com.google.appengine.api.search.Document.Builder builder = com.google.appengine.api.search.Document.newBuilder()
	        .addField(Field.newBuilder().setName(Question.ID).setText(request.get(Question.ID)))
	        .addField(Field.newBuilder().setName(Question.TITLE).setText(request.get(Question.TITLE)))
	        .addField(Field.newBuilder().setName(Question.DETAILS).setHTML(request.get(Question.DETAILS)));

		com.google.appengine.api.search.Document doc = builder.build();

		try {
			QuestionSearchIndex.QUESTION_INDEX.put(doc);
			response.put(System.RESULT_CODE, "0");
			response.put(System.RESULT_MSG, "Success");
		} catch (RuntimeException e) {
			if (response == null) {
				response = new Message(request.getContext());
			}

			response.put(System.RESULT_CODE, "-1");
			response.put(System.RESULT_MSG, "Couldn't add document to search index: " + e.getMessage());
		}
	}

	@Override
	protected void get(Message request, Message response) {
		Query query = Query.newBuilder()
			.setOptions(QueryOptions.newBuilder()
					//.setLimit(limit).
					.build())
			.build(request.get(QuestionSearchIndex.QUERY_STRING));

		Results<ScoredDocument> results = QuestionSearchIndex.QUESTION_INDEX.search(query);

		if (response == null) {
			response = new Message(request.getContext());
		}

		Message questionReq, questionResp, profileReq, profileResp;

		// process results
		int i = 0;
		String prefix = new String();
		for (ScoredDocument doc : results) {
			prefix = Question.RESOURCE_NAME + "[" + i + "].";

			response.put(prefix + Question.ID, doc.getOnlyField(Question.ID).getText());
			response.put(prefix + Question.TITLE, doc.getOnlyField(Question.TITLE).getText());
			response.put(prefix + Question.DETAILS, doc.getOnlyField(Question.DETAILS).getHTML());

			// retrieve other question details
			questionReq = new Message(request.getContext());
			questionResp = new Message(request.getContext());
			questionReq.put(Question.ID, doc.getOnlyField(Question.ID).getText());
			new QuestionService().doGet(questionReq, questionResp);

			if (questionResp.getReferences(Question.ANSWERS) != null) {
				response.put(prefix + Question.ANSWERS, "" + questionResp.getReferences(Question.ANSWERS).size());
			} else {
				response.put(prefix + Question.ANSWERS, "0");
			}
			response.put(prefix + Document.AUTHOR, questionResp.get(Document.AUTHOR));
			response.put(prefix + System.TIMESTAMP, questionResp.get(System.TIMESTAMP));
			
			// retrieve author's name
			profileReq = new Message(request.getContext());
			profileResp = new Message(request.getContext());
			profileReq.put(Profile.ID, questionResp.get(Document.AUTHOR));
			new ProfileService().doGet(profileReq, profileResp);
			response.put(prefix + Profile.FULL_NAME, profileResp.get(Profile.FULL_NAME));
			response.put(prefix + Profile.IMAGE_KEY, profileResp.get(Profile.IMAGE_KEY));

			i++;
		}

		response.put(QuestionSearchIndex.RESULTS, "" + i);

		response.put(System.RESULT_CODE, "0");
		response.put(System.RESULT_MSG, "Success");
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

	@Override
	protected void put(Message request, Message response) {
		// TODO Auto-generated method stub
		if (response == null) {
			response = new Message(request.getContext());
		}

		response.put(System.RESULT_CODE, "-1");
		response.put(System.RESULT_MSG, "Not implemented");
	}

}
