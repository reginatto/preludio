package com.preludio.services;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Service {

	protected static final Logger log = Logger.getLogger(Service.class.getName());

	protected abstract void post(Message request, Message response);
	protected abstract void get(Message request, Message response);
	protected abstract void put(Message request, Message response);
	protected abstract void delete(Message request, Message response);

	protected abstract String getName();

	// template method
	public void doPost(Message request, Message response) {
		// pre-processing
		if (log.isLoggable(Level.INFO)) {
			log.info(this.getName() + ":POST fired with request " + request.toJson());
		}

		// service execution
		this.post(request, response);

		// post-processing
		this.afterPost(request, response);
		if (log.isLoggable(Level.INFO)) {
			log.info(this.getName() + ":POST returned with response " + response.toJson());
		}
	}

	protected void afterPost(Message request, Message response) {
	}

	// template method
	public void doGet(Message request, Message response) {
		// pre-processing
		if (log.isLoggable(Level.INFO)) {
			log.info(this.getName() + ":GET fired with request " + request.toJson());
		}

		// service execution
		this.get(request, response);

		// post-processing
		this.afterGet(request, response);
		if (log.isLoggable(Level.INFO)) {
			log.info(this.getName() + ":GET returned with response " + response.toJson());
		}
	}

	protected void afterGet(Message request, Message response) {
	}

	// template method
	public void doPut(Message request, Message response) {
		// pre-processing
		if (log.isLoggable(Level.INFO)) {
			log.info(this.getName() + ":PUT fired with request " + request.toJson());
		}

		// service execution
		this.put(request, response);

		// post-processing
		this.afterPut(request, response);
		if (log.isLoggable(Level.INFO)) {
			log.info(this.getName() + ":PUT returned with response " + response.toJson());
		}
	}

	protected void afterPut(Message request, Message response) {
	}

	// template method
	public void doDelete(Message request, Message response) {
		// pre-processing
		if (log.isLoggable(Level.INFO)) {
			log.info(this.getName() + ":DELETE fired with request " + request.toJson());
		}

		// service execution
		this.delete(request, response);

		// post-processing
		if (log.isLoggable(Level.INFO)) {
			log.info(this.getName() + ":DELETE returned with response " + response.toJson());
		}
	}
}
