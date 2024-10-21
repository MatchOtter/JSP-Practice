package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class UserDAO {
	
	DataSource ds;
	
	public UserDAO () {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/UserChat");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int login(String userID, String userPw) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM USER WHERE userID = ?";
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				if(rs.getString("userPw").equals(userPw)) {
					return 1; //로그인에 성공
				}
				return 2; //비밀번호가 틀림
			}else {
				return 0; // 해당 사용자가 존재하지 않음
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return -1; // 데이터 베이스 오류
	}
	
	
	
	public int registerCheck(String userID) {
		System.out.println("UserDAO registerCheck userID : " + userID);
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM USER WHERE userID = ?";
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next() ||userID.equals("")) {
				return 0; //이미 존재하는 회원
			}else {
				System.out.println("UserDAO registerCheck userID : " + userID +" 가입가능");
				return 1; //가입 가능한 회원 아이디
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return -1; // 데이터 베이스 오류
	}
	
	
	public int register(String userID, String userPw, String userName, String userAge, String userGender, String userEmail, String userProfile) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    String sql = "INSERT INTO USER VALUES(?, ?, ?, ?, ?, ?, ?)";
	    
	    try {
	        conn = ds.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userID);
	        pstmt.setString(2, userPw);
	        pstmt.setString(3, userName);
	        pstmt.setInt(4, Integer.parseInt(userAge));
	        pstmt.setString(5, userGender);
	        pstmt.setString(6, userEmail);
	        pstmt.setString(7, userProfile);

	        int result = pstmt.executeUpdate();
	        return result; // 성공 시 1 반환 (insert 성공한 row 수)

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        } catch (Exception e2) {
	            e2.printStackTrace();
	        }
	    }
	    return -1; // 데이터베이스 오류
	}

	
	public UserDTO getUser(String userID) {
		System.out.println("UserDAO getUser userID : " + userID);
		
		UserDTO user = new UserDTO();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM USER WHERE userID = ?";
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next() ||userID.equals("")) {
				user.setUserID(userID);
				user.setUserPw(rs.getString("userPw"));
				user.setUserName(rs.getString("userName"));
				user.setUserAge(rs.getInt("userAge"));
				user.setUserGender(rs.getString("userGender"));
				user.setUserEmail(rs.getString("userEmail"));
				user.setUserProfile(rs.getString("userProfile"));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return user; 
	}
	
	
	
	public int update(String userID, String userPw, String userName, String userAge, String userGender, String userEmail) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    String sql = "UPDATE USER SET userPw = ?, userName = ?, userAge = ?, userGender = ?, userEmail = ?"
	    			+ "where userID = ?";
	    
	    try {
	        conn = ds.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userPw);
	        pstmt.setString(2, userName);
	        pstmt.setInt(3, Integer.parseInt(userAge));
	        pstmt.setString(4, userGender);
	        pstmt.setString(5, userEmail);
	        pstmt.setString(6, userID);

	        int result = pstmt.executeUpdate();
	        return result; // 성공 시 1 반환 (insert 성공한 row 수)

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        } catch (Exception e2) {
	            e2.printStackTrace();
	        }
	    }
	    return -1; // 데이터베이스 오류
	}
	
	
	public int profile(String userID,  String userProfile) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "UPDATE USER SET userProfile = ? where userID = ?";
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userProfile);
			pstmt.setString(2, userID);

			
			int result = pstmt.executeUpdate();
			return result; // 성공 시 1 반환 (insert 성공한 row 수)
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return -1; // 데이터베이스 오류
	}
	
	
	public String getProfile(String userID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM USER WHERE userID = ?";
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				if(rs.getString("userProfile").equals("")) {
					return "http://localhost:8888/UserChat/images/1.png";
				}
				return "http://localhost:8888/UserChat/upload/"+ rs.getString("userProfile");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return "http://localhost:8888/UserChat/images/icon.1.png";
	}
	
	
}//
