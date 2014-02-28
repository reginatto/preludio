package com.preludio.services.admin.event;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Event {

	public static final String RESOURCE_NAME = "event";

	public static final String ID = "event_id";
	public static final String DOCUMENT_TYPE = "event_document_type";
	public static final String TYPE = "event_type";
	public static final String DESCRIPTION = "event_description";
	public static final String OBJECT = "event_object";
	public static final String USER = "event_user";
	public static final String TIMESTAMP = "event_timestamp";

	@PrimaryKey
	@Persistent
	private String id;
	@Persistent
	private String docType;
	@Persistent
	private String type;
	@Persistent
	private String description;
	@Persistent
	private String object;
	@Persistent
	private String user;
	@Persistent
	private Long timestamp;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
