package chat;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLDecoder;
@WebServlet("/ChatUnreadServlet")
public class ChatUnreadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String userID = request.getParameter("userID");
		
		if(userID == null || userID.equals("")) {
			response.getWriter().write("0");
			
		}else {
			HttpSession session =request.getSession();
			String decodingUserID = URLDecoder.decode(userID, "UTF-8");
			
			if(!decodingUserID.equals((String)session.getAttribute("userID"))) {
				response.getWriter().write("");
				return;
			}
			userID= URLDecoder.decode(userID, "UTF-8");
			
			response.getWriter().write(new ChatDAO().getAllUnreadChat(userID)+ "");
		}
		
	}


}
