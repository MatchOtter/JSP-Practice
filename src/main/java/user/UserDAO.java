package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UserDAO {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public UserDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/AJAX";
			String dbID = "root";
			String dbPassword = "sha1419857!";
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
					
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<User> search(String userName) {
	    System.out.println("search userName :" + userName);
	    
	    String SQL = "SELECT * FROM USER WHERE userName LIKE ?";
	    ArrayList<User> userList = new ArrayList<User>();
	    try {
	        pstmt = conn.prepareStatement(SQL);
	        pstmt.setString(1, "%" + userName + "%");  // 와일드카드로 검색
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            do {
	                System.out.println("UserName: " + rs.getString(1) + ", Age: " + rs.getInt(2) + 
	                                   ", Gender: " + rs.getString(3) + ", Email: " + rs.getString(4));
	                User user = new User();
	                user.setUserName(rs.getString(1));
	                user.setUserAge(rs.getInt(2));
	                user.setUserGender(rs.getString(3));
	                user.setUserEmail(rs.getString(4));
	                
	                userList.add(user);
	            } while (rs.next());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return userList;
	}
	
	
	public int register(User user) {
		String SQL = "INSERT INTO USER VALUES(?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserName());
			pstmt.setInt(2, user.getUserAge());
			pstmt.setString(3, user.getUserGender());
			pstmt.setString(4, user.getUserEmail());
			
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터 베이스 오류
	}
	
	
}
