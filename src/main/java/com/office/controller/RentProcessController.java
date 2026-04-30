package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// import com.office.dao.RentalDAO;
// import com.office.dto.RentalHistoryDTO;

@WebServlet("/rentProcess.do")
public class RentProcessController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 1. 폼에서 넘어온 데이터 받기
        String eqNoStr = request.getParameter("eqNo");
        String empNoStr = request.getParameter("empNo");
        String rentalDateStr = request.getParameter("rentalDate");
        String returnDateStr = request.getParameter("returnDate");

        PrintWriter out = response.getWriter();
        out.println("<script>");

        try {
            int eqNo = Integer.parseInt(eqNoStr);
            int empNo = Integer.parseInt(empNoStr);
            Date rentalDate = Date.valueOf(rentalDateStr);
            Date returnDate = Date.valueOf(returnDateStr);

            // DB 연동 전 임시 테스트 로직 (무조건 성공 처리)
            boolean isSuccess = true;

            /*
            // [나중에 집에 가셔서 DB 연결할 때 사용할 실제 코드]
            RentalHistoryDTO dto = new RentalHistoryDTO();
            dto.setEqNo(eqNo);
            dto.setEmpNo(empNo);
            dto.setRentalDate(rentalDate);
            dto.setReturnDate(returnDate);
            dto.setStatus("승인대기"); // 초기 상태 설정
            dto.setApprovalStep(1);   // 1차 결재 대기

            RentalDAO dao = new RentalDAO();
            boolean isSuccess = dao.insertRental(dto);
            */

            // 2. 결과 처리
            if (isSuccess) {
                out.println("alert('비품 대여 결재가 성공적으로 기안되었습니다. (관리자 승인 대기 중)');");
                // 실제로는 내 대여 내역(myRentalList.do)으로 이동하는 것이 좋습니다.
                out.println("location.href='main.jsp';"); 
            } else {
                out.println("alert('결재 기안 중 오류가 발생했습니다.');");
                out.println("history.back();");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("alert('데이터 처리 중 오류가 발생했습니다. 날짜 형식을 확인해주세요.');");
            out.println("history.back();");
        }
        
        out.println("</script>");
        out.flush();
        out.close();
    }
}