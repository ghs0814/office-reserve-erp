package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.office.dao.RentalDAO;

/**
 * 비품 반납 요청을 처리하는 컨트롤러입니다.
 */
@WebServlet("/returnProcess.do")
public class ReturnProcessController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 한글 깨짐 방지 및 응답 형식 설정
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 1. 파라미터로 전달된 대여 번호(rentalNo)를 가져옵니다.
        String rentalNoStr = request.getParameter("rentalNo");

        PrintWriter out = response.getWriter();
        out.println("<script>");

        if (rentalNoStr != null) {
            int rentalNo = Integer.parseInt(rentalNoStr);
            RentalDAO rentalDao = new RentalDAO();
            
            // 2. DAO를 호출하여 DB 내 대여 상태를 '반납완료'로 업데이트합니다.
            boolean isSuccess = rentalDao.updateStatus(rentalNo, "반납완료");
            
            // 3. 처리 결과에 따른 알림 및 페이지 이동
            if(isSuccess) {
                out.println("alert('" + rentalNo + "번 비품이 성공적으로 반납 처리되었습니다.');");
            } else {
                out.println("alert('반납 처리에 실패했습니다.');");
            }
            // 4. 반납 완료 후 다시 내 대여 목록 화면으로 돌아갑니다.
            out.println("location.href='myRentalList.do';"); 
            
        } else {
            // 잘못된 접근 시 이전 화면으로 보냅니다.
            out.println("alert('잘못된 접근입니다.');");
            out.println("history.back();");
        }
        
        out.println("</script>");
        out.flush();
        out.close();
    }
}