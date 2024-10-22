package board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	
	DataSource ds;
	
	public BoardDAO () {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/UserChat");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public int write(String userID, String boardTitle, String boardContent, String boardFile, String boardRealFile) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    String sql = "INSERT INTO BOARD (userID, boardID, boardTitle, boardContent, boardDate, boardHit, boardFile, boardRealFile, boardGroup, boardSequence, boardLevel, boardAvailable) "
	            + "SELECT ?, IFNULL(MAX(boardID) + 1, 1), ?, ?, now(), 0, ?, ?, IFNULL(MAX(boardGroup) + 1, 0), 0, 0, 1 FROM BOARD";

	    
	    try {
	        conn = ds.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        
	        pstmt.setString(1, userID);
	        pstmt.setString(2, boardTitle);
	        pstmt.setString(3, boardContent);
	        pstmt.setString(4, boardFile);
	        pstmt.setString(5, boardRealFile);

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
	
	
	
	public int hit(String boardID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "UPDATE BOARD SET boardHit = boardHit + 1 WHERE boardID = ?";
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, boardID);
			
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

	
	public BoardDTO getBoard(String boardID) {
		BoardDTO board = new BoardDTO();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM board WHERE boardID = ?";
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				board.setUserID(rs.getString("userID"));
				board.setBoardID(rs.getInt("boardID"));
				board.setBoardTitle(rs.getString("boardTitle")
						.replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;")
						.replaceAll(">", "&gt;")
						.replaceAll("\n", "<br>"));
				board.setBoardContent(rs.getString("boardContent")
						.replaceAll(" ", "&nbsp;")
						.replaceAll("<", "&lt;")
						.replaceAll(">", "&gt;")
						.replaceAll("\n", "<br>"));
				board.setBoardDate(rs.getString("boardDate").substring(0,11));
				board.setBoardHit(rs.getInt("boardHit")); 
				board.setBoardFile(rs.getString("boardFile"));
				board.setBoardRealFile(rs.getString("boardRealFile"));
				board.setBoardGroup(rs.getInt("boardGroup"));
				board.setBoardSequence(rs.getInt("boardSequence"));
				board.setBoardLevel(rs.getInt("boardLevel"));
				board.setBoardAvailable(rs.getInt("boardAvailable"));

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
		return board; 
	}
	
	
	public ArrayList<BoardDTO> getList(String pageNumber) {
	    ArrayList<BoardDTO> boardList = new ArrayList<>();
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    String sql = "SELECT * FROM board WHERE boardGroup > "
	            + "(SELECT MAX(boardGroup) FROM BOARD) - ? "
	            + "AND boardGroup <= (SELECT MAX(boardGroup) FROM BOARD) - ? "
	            + "ORDER BY boardGroup DESC, boardSequence ASC"; 

	    try {
	        conn = ds.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, Integer.parseInt(pageNumber) *10);
	        pstmt.setInt(2, (Integer.parseInt(pageNumber)-1) * 10);
	        rs = pstmt.executeQuery();

	        while (rs.next()) {  // 반복문을 사용하여 여러 게시물을 처리
	            BoardDTO board = new BoardDTO();
	            board.setUserID(rs.getString("userID"));
	            board.setBoardID(rs.getInt("boardID"));
	            board.setBoardTitle(rs.getString("boardTitle")
	                    .replaceAll(" ", "&nbsp;")
	                    .replaceAll("<", "&lt;")
	                    .replaceAll(">", "&gt;")
	                    .replaceAll("\n", "<br>"));
	            board.setBoardContent(rs.getString("boardContent")
	                    .replaceAll(" ", "&nbsp;")
	                    .replaceAll("<", "&lt;")
	                    .replaceAll(">", "&gt;")
	                    .replaceAll("\n", "<br>"));
	            board.setBoardDate(rs.getString("boardDate").substring(0, 11));
	            board.setBoardHit(rs.getInt("boardHit"));
	            board.setBoardFile(rs.getString("boardFile"));
	            board.setBoardRealFile(rs.getString("boardRealFile"));
	            board.setBoardGroup(rs.getInt("boardGroup"));
	            board.setBoardSequence(rs.getInt("boardSequence"));
	            board.setBoardLevel(rs.getInt("boardLevel"));
	            board.setBoardAvailable(rs.getInt("boardAvailable"));

	            boardList.add(board);  // 리스트에 추가
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        } catch (Exception e2) {
	            e2.printStackTrace();
	        }
	    }
	    return boardList;  // 전체 리스트 반환
	}

	
	

	public String getFile(String boardID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT boardFile FROM board WHERE boardID = ?";
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString("boardFile");
			}
			return "";
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
		return ""; 
	}
	
	
	public boolean nextPage(String pageNumber) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM board WHERE boardGroup >= ?";
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(pageNumber) * 10);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return true;
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
		return false; 
	}
	
	
	public int targetPage(String pageNumber) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT COUNT(boardGroup) FROM BOARD WHERE boardGroup > ?";
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (Integer.parseInt(pageNumber)-1) * 10);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt(1)/10;
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
		return 0; 
	}
	
	
	
	
	public String getRealFile(String boardID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT boardRealFile FROM board WHERE boardID = ?";
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString("boardRealFile");
			}
			return "";
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
		return ""; 
	}
	
	
	public int update(String boardID, String boardTitle, String boardContent, String boardFile, String boardRealFile) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    String sql ="UPDATE BOARD SET boardTitle =?, boardContent =?, boardFile =?, boardRealFile =?"
	    			+ "where boardID =?";

	    try {
	        conn = ds.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        
	        pstmt.setString(1, boardTitle);
	        pstmt.setString(2, boardContent);
	        pstmt.setString(3, boardFile);
	        pstmt.setString(4, boardRealFile);
	        pstmt.setInt(5, Integer.parseInt(boardID));

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
	
	
	
	public int delete(String boardID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql ="UPDATE  BOARD SET boardAvailable = 0 WHERE boardID = ?";
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, Integer.parseInt(boardID));
			
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
	
	
	
	public int reply(String userID, String boardTitle, String boardContent, String boardFile, String boardRealFile, BoardDTO parent) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;

	    // 먼저, 답글의 위치를 업데이트
	    replyUpdate(parent);
	    
	    String sql = "INSERT INTO BOARD (userID, boardID, boardTitle, boardContent, boardDate, boardHit, boardFile, boardRealFile, boardGroup, boardSequence, boardLevel) "
	               + "VALUES (?, (SELECT IFNULL(MAX(b.boardID), 0) + 1 FROM (SELECT boardID FROM BOARD) b), ?, ?, now(), 0, ?, ?, ?, ?, ?, 1)";

	    try {
	        conn = ds.getConnection();
	        pstmt = conn.prepareStatement(sql);

	        pstmt.setString(1, userID);
	        pstmt.setString(2, boardTitle);
	        pstmt.setString(3, boardContent);
	        pstmt.setString(4, boardFile);
	        pstmt.setString(5, boardRealFile);
	        pstmt.setInt(6, parent.getBoardGroup());
	        pstmt.setInt(7, parent.getBoardSequence() + 1);
	        pstmt.setInt(8, parent.getBoardLevel() + 1);

	        int result = pstmt.executeUpdate();
	        return result;

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
	    return -1;
	}


	
	
	
	public int replyUpdate(BoardDTO parent) {
		System.out.println("BoardDAO replyUpdate parent start");
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    String sql = "UPDATE BOARD SET boardSequence = boardSequence + 1 WHERE boardGroup = ? AND boardSequence > ?";

	    try {
	        conn = ds.getConnection();
	        pstmt = conn.prepareStatement(sql);

	        pstmt.setInt(1, parent.getBoardGroup());  // 같은 그룹에서
	        pstmt.setInt(2, parent.getBoardSequence());  // 부모 게시글보다 큰 시퀀스를 가진 게시글들에 대해

	        int result = pstmt.executeUpdate();
	        return result; // 성공 시 1 반환

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

	
	
	
}
