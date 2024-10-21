package chat;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@WebServlet("/ChatListServlet")
public class ChatListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String listType = request.getParameter("listType");
		
		if(listType ==null || listType.equals("")) {
			response.getWriter().write("");
		}else if(listType.equals("today")){response.getWriter().write(getToday());
		}else if(listType.equals("ten")){response.getWriter().write(getTen());
		}else {
			try {
				Integer.parseInt(listType);
				response.getWriter().write(getID(listType));
			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().write("");
			}
		}
	}

	public String getToday() {
		System.out.println("ChatListServlet getToday  start");
		StringBuffer result = new StringBuffer();
		result.append("{\"result\":[");
		ChatDAO chatDAO = new ChatDAO();
		ArrayList<Chat> chatList = chatDAO.getChatList(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		
		for(int i = 0; i < chatList.size(); i++) {
		    result.append("[{\"value\":\"" + chatList.get(i).getChatName() + "\"},");
		    result.append("{\"value\":\"" + chatList.get(i).getChatContent() + "\"},");
		    result.append("{\"value\":\"" + chatList.get(i).getChatTime() + "\"}]");
		    if(i != chatList.size() - 1) result.append(",");
		}
		result.append("],\"last\":\""+ chatList.get(chatList.size()-1).getChatID()+"\"}");
		
		return result.toString();
	}
	
	
	public String getTen() {
		System.out.println("ChatListServlet getTen  start");
		StringBuffer result = new StringBuffer();
		result.append("{\"result\":[");
		ChatDAO chatDAO = new ChatDAO();
		ArrayList<Chat> chatList = chatDAO.getChatListByRecent(10);
		
		for(int i = 0; i < chatList.size(); i++) {
			result.append("[{\"value\":\"" + chatList.get(i).getChatName() + "\"},");
			result.append("{\"value\":\"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\":\"" + chatList.get(i).getChatTime() + "\"}]");
			if(i != chatList.size() - 1) result.append(",");
		}
		result.append("],\"last\":\""+ chatList.get(chatList.size()-1).getChatID()+"\"}");
		
		return result.toString();
	}
	
	
	public String getID(String chatID) {
		System.out.println("ChatListServlet getID chatID : " + chatID);
		StringBuffer result = new StringBuffer();
		result.append("{\"result\":[");
		ChatDAO chatDAO = new ChatDAO();
		
		ArrayList<Chat> chatList = chatDAO.getChatListByRecent(chatID);
		
		System.out.println("ChatListServlet getID LOGGING start");
	    if (chatList.isEmpty()) {
	        System.out.println("ChatListServlet getID: chatList is empty");
	    } else {
	        System.out.println("ChatListServlet getID: chatList contains " + chatList.size() + " messages");
	    }
		
		for(int i = 0; i < chatList.size(); i++) {
			result.append("[{\"value\":\"" + chatList.get(i).getChatName() + "\"},");
			result.append("{\"value\":\"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\":\"" + chatList.get(i).getChatTime() + "\"}]");
			if(i != chatList.size() - 1) result.append(",");
		}
//		result.append("],\"last\":\""+ chatList.get(chatList.size()-1).getChatID()+"\"}");
	    // Check if chatList is empty
	    if (!chatList.isEmpty()) {
	        result.append("],\"last\":\"" + chatList.get(chatList.size() - 1).getChatID() + "\"}");
	    } else {
	        result.append("],\"last\":\"0\"}");
	    }
		
		return result.toString();
	}
	
	
	
	
	
}
