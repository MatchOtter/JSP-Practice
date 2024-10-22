package user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/UserUpdateServlet")
public class UserUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String userID = request.getParameter("userID");
		HttpSession session = request.getSession();

		String userPw1 = request.getParameter("userPw1");
		String userPw2 = request.getParameter("userPw2");
		String userName = request.getParameter("userName");
		String userAge = request.getParameter("userAge");
		String userGender = request.getParameter("userGender");
		String userEmail = request.getParameter("userEmail");
		
		if(userID == null || userID.equals("") ||
				userID == null || userID.equals("") ||
				userPw1 == null || userPw1.equals("") ||
				userPw2 == null || userPw2.equals("") ||
				userName == null || userName.equals("") ||
				userAge == null || userAge.equals("") ||
				userGender == null || userGender.equals("") ||
				userEmail == null || userEmail.equals("")
				) {
			request.getSession().setAttribute("messageType", "오류 메세지");
			request.getSession().setAttribute("messageContent", "모든 내용을 입력하세요");
			response.sendRedirect("update.jsp");
			return;
		}
		
		if(!userID.equals((String)session.getAttribute("userID"))) {
			session.setAttribute("messageType", "오류 메세지");
			session.setAttribute("messageContent", "접근할 수 없습니다.");
			response.sendRedirect("index.jsp");
			return;
		}
		
		if (!userPw1.equals(userPw2)) {
			request.getSession().setAttribute("messageType", "오류 메세지");
			request.getSession().setAttribute("messageContent", "비밀번호가 서로 다릅니다");
			response.sendRedirect("update.jsp");
		}
		
		int result = new UserDAO().update(userID, userPw1, userName, userAge, userGender, userEmail);
		if(result == 1) {
			request.getSession().setAttribute("userID", userID);
			request.getSession().setAttribute("messageType", "성공 메세지");
			request.getSession().setAttribute("messageContent", "회원 정보 수정에 성공했습니다.");
			response.sendRedirect("index.jsp");
		}else {
			request.getSession().setAttribute("messageType", "오류 메세지");
			request.getSession().setAttribute("messageContent", "디비 오류가 발생했습니다.");
			response.sendRedirect("update.jsp");
		}
	}

	
	
}
