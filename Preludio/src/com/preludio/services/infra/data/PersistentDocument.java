package com.preludio.services.infra.data;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class PersistentDocument extends Document {

	@PrimaryKey
	@Persistent
	private String id;
	@Persistent
	private String userId;
	@Persistent
	private String type;
	@Persistent
	private Text content;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Text getContent() {
		return content;
	}
	public void setContent(Text content) {
		this.content = content;
	}

	public String getContentAsString() {
		return content.getValue();
	}

	public void setContentString(String contentStr) {
		this.content = new Text(contentStr);
	}
}
