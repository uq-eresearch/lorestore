package net.metadata.openannotation.lorestore.security.drupal;

public class DrupalUser {

	private String name;
	private String email;
	private String username;
	private String uid;
	private String drupalHostname;
	private boolean administrator;
	
	public String getPrimaryUri() {
		return "http://" + drupalHostname + "/user/" + uid;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isAdministrator() {
		return administrator;
	}

	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}

	public String getDrupalHostname() {
		return drupalHostname;
	}

	public void setDrupalHostname(String drupalHostname) {
		this.drupalHostname = drupalHostname;
	}
	
}
