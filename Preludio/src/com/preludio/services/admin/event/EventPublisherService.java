package com.preludio.services.admin.event;

import com.preludio.services.infra.pubsub.PublisherService;

public class EventPublisherService extends PublisherService {

	public static final String EVENT_QUEUE = "events";
	private static final String EVENT_WORKER_URL = "/q/events/worker";

	@Override
	protected String getQueueName() {
		return EVENT_QUEUE;
	}

	@Override
	protected String getWorkerURL() {
		return EVENT_WORKER_URL;
	}

}
