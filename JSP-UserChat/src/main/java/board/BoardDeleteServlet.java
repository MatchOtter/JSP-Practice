package board;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
@WebServlet("/BoardDeleteServlet")
public class BoardDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		HttpSession session = request.getSession();
		String userID = (String)session.getAttribute("userID");
		String boardID = request.getParameter("boardID");
		
		if(boardID == null || boardID.equals("")) {
			request.getSession().setAttribute("messageType", "오류 메세지");
			request.getSession().setAttribute("messageContent", "접근할 수 없습니다");
			response.sendRedirect("login.jsp");
			return;
		}
		
		
		BoardDAO boardDAO = new BoardDAO();
		BoardDTO board =  boardDAO.getBoard(boardID);
		
        if(!userID.equals(board.getUserID())) {
            session.setAttribute("messageType", "오류 메세지");
            session.setAttribute("messageContent", "접근할 수 없습니다.");
            response.sendRedirect("index.jsp");
            return;
        }
        
        String savePath = request.getServletContext().getRealPath("/upload");
		String prev = boardDAO.getRealFile(boardID);
		int result = boardDAO.delete(boardID);
		
		if (result == -1) {
            session.setAttribute("messageType", "오류 메세지");
            session.setAttribute("messageContent", "접근할 수 없습니다.");
            response.sendRedirect("index.jsp");
            return;
		} else {
			File prevFile = new File(savePath + "/" + prev);
			if (prevFile.exists()) {
				prevFile.delete();
			}
			request.getSession().setAttribute("messageType", "성공 메세지");
			request.getSession().setAttribute("messageContent", "삭제에 성공 했습니다");
			response.sendRedirect("boardView.jsp");
            return;
		}

	}


}
