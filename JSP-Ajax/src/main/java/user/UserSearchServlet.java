package user;

import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/UserSearchServlet")
public class UserSearchServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String userName = request.getParameter("userName");
		
		System.out.println("doPost userName :"+userName);
		
		response.getWriter().write(getJSON(userName));
	}
	
	public String getJSON(String userName) {
		if(userName == null) userName = "";
				
		StringBuffer result = new StringBuffer("");
		result.append("{\"result\":[");
		UserDAO userDAO = new UserDAO();
		ArrayList<User> userList = userDAO.search(userName);
		
		for (int i = 0; i < userList.size(); i++) {
		    result.append("{\"userName\":\"" + userList.get(i).getUserName() + "\",");
		    result.append("\"userAge\":\"" + userList.get(i).getUserAge() + "\",");
		    result.append("\"userGender\":\"" + userList.get(i).getUserGender() + "\",");
		    result.append("\"userEmail\":\"" + userList.get(i).getUserEmail() + "\"}");
		    if (i != userList.size() - 1) {
		        result.append(",");
		    }
		}

		result.append("]}");
		
		
		System.out.println("userList userName :"+result.toString());

		
		return result.toString();	
	}
	
	
	
	
	
	
}
