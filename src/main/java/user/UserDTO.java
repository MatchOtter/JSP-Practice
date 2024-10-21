package user;

public class UserDTO {
	String userID;
	String userPassword;
	String userName;
	int uerAge;
	String userGender;
	String userEmai;
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getUerAge() {
		return uerAge;
	}
	public void setUerAge(int uerAge) {
		this.uerAge = uerAge;
	}
	public String getUserGender() {
		return userGender;
	}
	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}
	public String getUserEmai() {
		return userEmai;
	}
	public void setUserEmai(String userEmai) {
		this.userEmai = userEmai;
	}
}
