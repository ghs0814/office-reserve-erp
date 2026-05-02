package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.office.dao.RentalDAO;
import com.office.dto.RentalHistoryDTO;

@WebServlet("/rentProcess.do")
public class RentProcessController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String eqNoStr = request.getParameter("eqNo");
        String empNoStr = request.getParameter("empNo");
        String rentalDateStr = request.getParameter("rentalDate");
        String returnDateStr = request.getParameter("returnDate");
        String title = request.getParameter("title"); 

        PrintWriter out = response.getWriter();
        out.println("<script>");

        try {
            int eqNo = Integer.parseInt(eqNoStr);
            int empNo = Integer.parseInt(empNoStr);
            Date rentalDate = Date.valueOf(rentalDateStr);
            Date returnDate = Date.valueOf(returnDateStr);

            javax.servlet.http.HttpSession session = request.getSession();
            com.office.dto.EmployeeDTO loginEmp = (com.office.dto.EmployeeDTO) session.getAttribute("loginEmp");

            RentalHistoryDTO dto = new RentalHistoryDTO();
            dto.setEqNo(eqNo);
            dto.setEmpNo(empNo);
            dto.setRentalDate(rentalDate);
            dto.setReturnDate(returnDate);
            dto.setTitle(title); 
            
            int myLevel = loginEmp.getEmpLevel(); 
            
            // ★ 새로 추가된 부분: 기안 시점의 오늘 날짜 생성
            Date today = new Date(System.currentTimeMillis());

            if (myLevel >= 5) {
                // 최고 관리자(5단계)가 기안할 경우 즉시 '대여중' 처리 및 모든 날짜 채우기
                dto.setStatus("대여중");
                dto.setApprovalStep(5);
                dto.setSign1(loginEmp.getEmpName()); dto.setSign1Date(today);
                dto.setSign2(loginEmp.getEmpName()); dto.setSign2Date(today);
                dto.setSign3(loginEmp.getEmpName()); dto.setSign3Date(today);
                dto.setSign4(loginEmp.getEmpName()); dto.setSign4Date(today);
                dto.setSign5(loginEmp.getEmpName()); dto.setSign5Date(today);
            } else {
                dto.setStatus("승인대기");
                dto.setApprovalStep(myLevel + 1);
                
                // 본인 직급까지 이름과 '오늘 날짜'를 동시에 세팅합니다.
                if (myLevel >= 1) { dto.setSign1(loginEmp.getEmpName()); dto.setSign1Date(today); }
                if (myLevel >= 2) { dto.setSign2(loginEmp.getEmpName()); dto.setSign2Date(today); }
                if (myLevel >= 3) { dto.setSign3(loginEmp.getEmpName()); dto.setSign3Date(today); }
                if (myLevel >= 4) { dto.setSign4(loginEmp.getEmpName()); dto.setSign4Date(today); }
            }
            
            RentalDAO dao = new RentalDAO();
            boolean isSuccess = dao.insertRental(dto);

            if (isSuccess) {
                out.println("alert('비품 대여 결재가 성공적으로 기안되었습니다.');");
                out.println("location.href='documentList.do';"); 
            } else {
                out.println("alert('결재 기안 중 오류가 발생했습니다. (재고 부족 등)');");
                out.println("history.back();");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("alert('데이터 처리 중 오류가 발생했습니다.');");
            out.println("history.back();");
        }
        
        out.println("</script>");
        out.flush();
        out.close();
    }
}