package user;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/UserRegisterServlet")
public class UserRegisterServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String userName = request.getParameter("userName");
		String userAge = request.getParameter("userAge");
		String userGender = request.getParameter("userGender");
		String userEmail = request.getParameter("userEmail");
		
		response.getWriter().write(register(userName, userAge, userGender, userEmail)+"");
	}
	
	public int register(String userName,String userAge,String userGender,String userEmail) {
		System.out.println("UserRegisterServlet register :"+ userName+ userAge+ userGender+ userEmail);
		User user = new User();
		
		try {
			user.setUserName(userName);
			user.setUserAge(Integer.parseInt(userAge));
			user.setUserGender(userGender);
			user.setUserEmail(userEmail);
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
		return new UserDAO().register(user);
	}
	
	
	
	
	
	
}
