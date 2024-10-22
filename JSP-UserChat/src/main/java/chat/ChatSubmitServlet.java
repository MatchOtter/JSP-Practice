package chat;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLDecoder;
@WebServlet("/ChatSubmitServlet")
public class ChatSubmitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String fromID = request.getParameter("fromID");
		String toID = request.getParameter("toID");
		String chatContent = request.getParameter("chatContent");
		
		System.out.println("ChatSubmitServlet doPost request, response start");
		
		if(fromID == null || fromID.equals("") ||
			toID == null || toID.equals("") ||
			chatContent == null || chatContent.equals("")) {
			
			response.getWriter().write("0");
			
		}else if (fromID.equals(toID)) {
			response.getWriter().write("-1");
			
		}else {
			HttpSession session =request.getSession();
			
			String decodingFromID = URLDecoder.decode(fromID, "UTF-8");
			
			if(!decodingFromID.equals((String)session.getAttribute("userID"))) {
				response.getWriter().write("");
				return;
			}
			
			fromID= URLDecoder.decode(fromID, "UTF-8");
			toID= URLDecoder.decode(toID, "UTF-8");
			chatContent= URLDecoder.decode(chatContent, "UTF-8");
			
			System.out.println("ChatSubmitServlet doPost fromID, toID, chatContent : " + fromID +" " +toID +" " + chatContent);
			
			response.getWriter().write(new ChatDAO().submit(fromID, toID, chatContent)+ "");
		}
		
	}


}
