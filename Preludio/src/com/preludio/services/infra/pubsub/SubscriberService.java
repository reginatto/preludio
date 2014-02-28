package com.preludio.services.infra.pubsub;

import com.preludio.services.Message;
import com.preludio.services.Service;
import com.preludio.services.admin.event.EventPublisherService;
import com.preludio.services.admin.event.EventService;

public class SubscriberService extends Service {

	@Override
	protected String getName() {
		return this.getClass().getName();
	}

	@Override
	protected void post(Message request, Message response) {
		if (request.get(PubSubMessage.QUEUE_NAME).equals(EventPublisherService.EVENT_QUEUE)) {
			Message serviceRequest = new Message(request.getContext());
			serviceRequest.fromJson(request.get(PubSubMessage.CONTENT));
			new EventService().doPost(serviceRequest, response);
		}
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
