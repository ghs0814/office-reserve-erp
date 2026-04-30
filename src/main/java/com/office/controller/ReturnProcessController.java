package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.office.dao.RentalDAO;

@WebServlet("/returnProcess.do")
public class ReturnProcessController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String rentalNoStr = request.getParameter("rentalNo");

        PrintWriter out = response.getWriter();
        out.println("<script>");

        if (rentalNoStr != null) {
            int rentalNo = Integer.parseInt(rentalNoStr);
            RentalDAO rentalDao = new RentalDAO();
            
            // 상태를 '반납완료'로 변경 실행
            boolean isSuccess = rentalDao.updateStatus(rentalNo, "반납완료");
            
            if(isSuccess) {
                out.println("alert('" + rentalNo + "번 비품이 성공적으로 반납 처리되었습니다.');");
            } else {
                out.println("alert('반납 처리에 실패했습니다.');");
            }
            out.println("location.href='myRentalList.do';"); 
            
        } else {
            out.println("alert('잘못된 접근입니다.');");
            out.println("history.back();");
        }
        
        out.println("</script>");
        out.flush();
        out.close();
    }
}