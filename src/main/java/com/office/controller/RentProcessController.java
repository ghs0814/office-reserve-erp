package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// [수정] 아래 두 줄의 주석(//)을 해제하여 DTO와 DAO를 정상적으로 불러옵니다.
import com.office.dao.RentalDAO;
import com.office.dto.RentalHistoryDTO;

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

            // 세션에서 로그인한 사원(기안자) 정보 가져오기
            javax.servlet.http.HttpSession session = request.getSession();
            com.office.dto.EmployeeDTO loginEmp = (com.office.dto.EmployeeDTO) session.getAttribute("loginEmp");

            RentalHistoryDTO dto = new RentalHistoryDTO();
            dto.setEqNo(eqNo);
            dto.setEmpNo(empNo);
            dto.setRentalDate(rentalDate);
            dto.setReturnDate(returnDate);
            
            // 핵심: 기안자는 1단계 승인자이므로 2단계로 상신하고 서명 1번에 본인 이름 세팅
            dto.setApprovalStep(2); 
            dto.setSign1(loginEmp.getEmpName());

            RentalDAO dao = new RentalDAO();
            boolean isSuccess = dao.insertRental(dto);

            // 2. 결과 처리
            if (isSuccess) {
                out.println("alert('비품 대여 결재가 성공적으로 기안되었습니다. (관리자 승인 대기 중)');");
                // [수정] 성공 시 내 대여 내역 페이지로 바로 이동하도록 변경
                out.println("location.href='myRentalList.do';"); 
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