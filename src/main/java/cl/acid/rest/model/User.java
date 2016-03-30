package cl.acid.rest.model;

public class User {

	private String username;
	
	private byte[] image;

	public String getUsername() {
		return username;
	}

	public byte[] getImage() {
		return image;
	}

	public User(String username, byte[] image) {
		super();
		this.username = username;
		this.image = image;
	}
	
	User(){}
}
