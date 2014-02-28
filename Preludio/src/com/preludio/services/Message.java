package com.preludio.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Message {

	private transient Context _ctx = new Context();

	private Map<String, String> _fields = new HashMap<String, String>();
	private Map<String, List<Map<String, String>>> _collections = new HashMap<String, List<Map<String, String>>>();

	protected Message() {
		// to be used by internal JSon reconstruct
	}

	public Message(Context ctx) {
		this.put(com.preludio.services.System.TIMESTAMP, "" + java.lang.System.currentTimeMillis());
		this.setContext(ctx);
	}

	public void setContext(Context ctx) {
		if (ctx != null) {
			this._ctx = ctx;
		}
		// Commenting below, it's a mistake.
		// Document author should be specifically set by callers,
		// it's different from logged in user.
		//this.put(Document.AUTHOR, this._ctx.getLoggedInUserProfileId());
	}

	public Context getContext() {
		return this._ctx;
	}

	public void putAllFields(Map<String, String> fields) {
		this._fields.putAll(fields);
	}

	public Map<String, String> getFields() {
		return this._fields;
	}

	public void putAllReferences(Map<String, List<Map<String, String>>> references) {
		for (String reference: references.keySet()) {
			if (this._collections.containsKey(reference)) {
				for (Map<String, String> fields : references.get(reference)) {
					this._collections.get(reference).add(fields);
				}
			} else {
				this._collections.put(reference, references.get(reference));
			}
		}
	}

	public Map<String, List<Map<String, String>>> getReferences() {
		return this._collections;
	}

	public void put(String key, String value) {
		this._fields.put(key, value);
	}

	public void putReference(String collection, String key, String value) {
		Map<String, String> _reference = new HashMap<String, String>();
		_reference.put(key, value);
		if (!this._collections.containsKey(collection)) {
			this._collections.put(collection, new ArrayList<Map<String,String>>());
		}
		this._collections.get(collection).add(_reference);
	}

	public String get(String key) {
		if (this._fields.containsKey(key)) {
			return this._fields.get(key);
		} else {
			return "";
		}
	}

	public List<Map<String, String>> getReferences(String collection) {
		if (this._collections.containsKey(collection)) {
			return this._collections.get(collection);
		} else {
			return new ArrayList<Map<String,String>>();
		}
	}

	public void remove(String key) {
		this._fields.remove(key);
	}

	public void removeReference(String collection, String key, String value) {
		Map<String, String> _reference = new HashMap<String, String>();
		_reference.put(key, value);
		this._collections.get(collection).remove(_reference);
	}

	public boolean containsField(String key) {
		return this._fields.containsKey(key);
	}

	public boolean containsReference(String collection, String key, String value) {
		Map<String, String> _reference = new HashMap<String, String>();
		_reference.put(key, value);
		return this._collections.get(collection).contains(_reference);
	}

	public boolean containsCollection(String collection) {
		return this._collections.containsKey(collection);
	}

	public String toJson() {
		return new Gson().toJson(this);
	}

	public void fromJson(String json) {
		Message temp = new Gson().fromJson(json, new TypeToken<Message>(){}.getType());
		this.putAllFields(temp._fields);
		this.putAllReferences(temp._collections);
	}
}
