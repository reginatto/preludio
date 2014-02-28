package com.preludio.services.infra.auth;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class AuthenticationInfo {

	public static final String RESOURCE_NAME = "authinfo";

	public static final String HASH = "authinfo_hash";

	@PrimaryKey
	@Persistent
	private String email;
	@Persistent
	private String profileId;
	@Persistent
	private String hash;

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getProfileId() {
		return profileId;
	}
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}

}
