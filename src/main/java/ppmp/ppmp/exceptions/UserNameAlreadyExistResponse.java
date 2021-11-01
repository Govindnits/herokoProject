package ppmp.ppmp.exceptions;

public class UserNameAlreadyExistResponse {
	private String username;

	public UserNameAlreadyExistResponse(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
