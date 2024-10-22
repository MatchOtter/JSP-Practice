package user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
@WebServlet("/UserLoginServlet")
public class UserLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String userID = request.getParameter("userID");
		String userPw = request.getParameter("userPw");
		
		if(userID == null || userID.equals("") ||
				userPw == null || userPw.equals("")
				) {
			request.getSession().setAttribute("messageType", "오류 메세지");
			request.getSession().setAttribute("messageContent", "모든 내용을 입력해주세요.");
			response.sendRedirect("login.jsp");
			return;
		}
		
		int result = new UserDAO().login(userID, userPw);
		
		if (result == 1) {
			request.getSession().setAttribute("userID", userID);
			System.out.println("UserLoginServlet doPost userID : " + userID);
			
			request.getSession().setAttribute("messageType", "성공 메세지");
			request.getSession().setAttribute("messageContent", "로그인에 성공했습니다.");
			response.sendRedirect("index.jsp");
			
		}else if (result == 2) {
			request.getSession().setAttribute("messageType", "오류 메세지");
			request.getSession().setAttribute("messageContent", "비밀번호를 다시 확인하세요.");
			response.sendRedirect("login.jsp");

		}else if (result == 0) {
			request.getSession().setAttribute("messageType", "오류 메세지");
			request.getSession().setAttribute("messageContent", "아이디가 존재하지 않습니다.");
			response.sendRedirect("login.jsp");
			
		}else if (result == -1) {
			request.getSession().setAttribute("messageType", "오류 메세지");
			request.getSession().setAttribute("messageContent", "디비 오류가 발생했습니다.");
			response.sendRedirect("login.jsp");
		}
		
	}


}
