package net.metadata.openannotation.lorestore.security.drupal;

public class DrupalUser {

	private String name;
	private String email;
	private String username;
	private String uid;
	private boolean administrator;
	
	public String getPrimaryUri() {
		return "http://localhost/user/1";
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
	
}
