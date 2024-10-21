package board;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/BoardReplyServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class BoardReplyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 파일 저장 경로 설정
        String savePath = request.getServletContext().getRealPath("/upload");

        // 경로가 없으면 생성
        File uploadDir = new File(savePath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // 세션과 사용자 확인
        HttpSession session = request.getSession();
        String userID = request.getParameter("userID");
        if (userID == null || !userID.equals((String) session.getAttribute("userID"))) {
            session.setAttribute("messageType", "오류 메세지");
            session.setAttribute("messageContent", "접근할 수 없습니다.");
            response.sendRedirect("index.jsp");
            return;
        }

        // 보드 아이디 확인
        String boardID = request.getParameter("boardID");
        if (boardID == null || boardID.equals("")) {
            session.setAttribute("messageType", "오류 메세지");
            session.setAttribute("messageContent", "내용을 모두 입력해주세요.");
            response.sendRedirect("index.jsp");
            return;
        }

        
        // 제목과 내용 확인
        String boardTitle = request.getParameter("boardTitle");
        String boardContent = request.getParameter("boardContent");
        if (boardTitle == null || boardTitle.equals("") || boardContent == null || boardContent.equals("")) {
            session.setAttribute("messageType", "오류 메세지");
            session.setAttribute("messageContent", "내용을 모두 입력해주세요.");
            response.sendRedirect("index.jsp");
            return;
        }

        // 파일 업로드 처리
        String boardFile = "";
        String boardRealFile = "";
        try {
            Part filePart = request.getPart("boardFile"); // JSP form input에서 가져옴
            if (filePart != null && filePart.getSize() > 0) {
                boardFile = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // 파일 이름 추출
                boardRealFile = System.currentTimeMillis() + "_" + boardFile; // 고유한 이름으로 변경

                // 파일 저장
                filePart.write(savePath + File.separator + boardRealFile);
            }

            // 게시글 작성 (DB 처리 - DAO 호출)
            BoardDAO boardDAO = new BoardDAO();
            BoardDTO parent = boardDAO.getBoard(boardID);
            boardDAO.replyUpdate(parent);
            boardDAO.reply(userID, boardTitle, boardContent, boardFile, boardRealFile, parent);

            // 성공 메세지 설정 후 리다이렉트
            session.setAttribute("messageType", "성공 메세지");
            session.setAttribute("messageContent", "게시물이 성공적으로 답변이 작성되었습니다.");
            response.sendRedirect("boardView.jsp");
            return;

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("messageType", "오류 메세지");
            session.setAttribute("messageContent", "파일 업로드 중 오류가 발생했습니다.");
            response.sendRedirect("boardWrite.jsp");
            return;
        }
    }
}
