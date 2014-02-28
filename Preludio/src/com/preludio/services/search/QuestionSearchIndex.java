package com.preludio.services.search;

import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;

public abstract class QuestionSearchIndex {

	public static final String RESOURCE_NAME = "questionSearchIndex";

	public static final String QUERY_STRING = "query_string";
	public static final String RESULTS = "query_results";

	/**
	 * Search index for questions used by the application. We build an
	 * index with the default consistency, which is Consistency.PER_DOCUMENT.
	 * These types of indexes are most suitable for streams and feeds, and
	 * can cope with a high rate of updates.
	 */
	public static final Index QUESTION_INDEX = SearchServiceFactory.getSearchService()
		.getIndex(IndexSpec.newBuilder().setName("question_index"));
}
