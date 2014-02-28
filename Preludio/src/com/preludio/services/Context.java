package com.preludio.services;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.preludio.services.profile.Profile;

@SuppressWarnings("serial")
public class Context implements Serializable {

	private Map<String, String> _fields;

	public Context() {
		this._fields = new HashMap<String, String>();
	}

	public void put(String key, String value) {
		this._fields.put(key, value);
	}

	public String get(String key) {
		if (this._fields.containsKey(key)) {
			return this._fields.get(key);
		} else {
			return "";
		}
	}

	public void remove(String key) {
		this._fields.remove(key);
	}

	public boolean containsField(String key) {
		return this._fields.containsKey(key);
	}

	public void setLoggedInUser(String profileId, String profileName) {
		this.put(Profile.ID, profileId);
		this.put(Profile.FULL_NAME, profileName);
	}

	public String getLoggedInUserProfileId() {
		return this.get(Profile.ID);
	}

	public String getLoggedInUserProfileFullName() {
		return this.get(Profile.FULL_NAME);
	}

	public boolean isUserLoggedIn() {
		return this.containsField(Profile.ID);
	}
}
