package com.preludio.services.infra.pubsub;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.preludio.services.Message;
import com.preludio.services.Service;

public abstract class PublisherService extends Service {

	protected abstract String getQueueName();

	protected abstract String getWorkerURL();

	@Override
	protected String getName() {
		return this.getClass().getName();
	}

	@Override
	protected void post(Message request, Message response) {
		Queue q = QueueFactory.getQueue(this.getQueueName());
		q.add(TaskOptions.Builder.withUrl(this.getWorkerURL()).method(
				Method.POST).param(PubSubMessage.CONTENT, request.toJson()));
	}

	@Override
	protected void get(Message request, Message response) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void put(Message request, Message response) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void delete(Message request, Message response) {
		// TODO Auto-generated method stub

	}

}
