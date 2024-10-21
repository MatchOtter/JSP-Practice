package user;

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

@WebServlet("/UserProfileServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class UserProfileServlet extends HttpServlet {
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

        // 파일 업로드 처리
        HttpSession session = request.getSession();
        String userID = request.getParameter("userID");
        if (!userID.equals((String) session.getAttribute("userID"))) {
            session.setAttribute("messageType", "오류 메세지");
            session.setAttribute("messageContent", "접근할 수 없습니다.");
            response.sendRedirect("index.jsp");
            return;
        }

        String fileName = "";
        try {
            Part filePart = request.getPart("userProfile");  // "userProfile"는 form input name
            if (filePart != null && filePart.getSize() > 0) {
                fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                // 파일 확장자 확인
                String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                if (ext.equals("jpg") || ext.equals("png") || ext.equals("gif")) {

                    // 기존 프로필 이미지 삭제
                    String prevProfile = new UserDAO().getUser(userID).getUserProfile();
                    File prevFile = new File(savePath + File.separator + prevProfile);
                    if (prevFile.exists()) {
                        prevFile.delete();
                    }

                    // 새 파일 저장
                    filePart.write(savePath + File.separator + fileName);
                } else {
                    session.setAttribute("messageType", "오류 메세지");
                    session.setAttribute("messageContent", "이미지 파일만 업로드 가능합니다.");
                    response.sendRedirect("profileUpdate.jsp");
                    return;
                }
            }
        } catch (Exception e) {
            session.setAttribute("messageType", "오류 메세지");
            session.setAttribute("messageContent", "파일 업로드 중 오류가 발생했습니다.");
            response.sendRedirect("profileUpdate.jsp");
            return;
        }

        // DB에 새 프로필 정보 저장
        new UserDAO().profile(userID, fileName);

        // 성공 메시지 설정
        session.setAttribute("messageType", "성공 메세지");
        session.setAttribute("messageContent", "성공적으로 프로필이 변경되었습니다.");
        response.sendRedirect("index.jsp");
    }
}
