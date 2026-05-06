package com.office.controller;

import java.io.*;
import java.sql.Date;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.office.dao.LeaveDAO;
import com.office.dto.*;

@WebServlet("/leaveProcess.do")
public class LeaveProcessController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");

        LocalDate start = LocalDate.parse(request.getParameter("startDate"));
        LocalDate end = LocalDate.parse(request.getParameter("endDate"));
        String reason = request.getParameter("reason");

        LeaveDAO dao = new LeaveDAO();
        int useDays = dao.calculateWorkingDays(start, end);

        if (useDays > loginEmp.getCurLeave()) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<script>alert('잔여 연차가 부족합니다.'); history.back();</script>");
            return;
        }

        LeaveHistoryDTO dto = new LeaveHistoryDTO();
        dto.setEmpNo(loginEmp.getEmpNo());
        dto.setStartDate(Date.valueOf(start));
        dto.setEndDate(Date.valueOf(end));
        dto.setUseDays(useDays);
        dto.setReason(reason);
        
        // ★ 비품 대여와 동일한 자동 도장 로직 적용
        int myLevel = loginEmp.getEmpLevel();
        Date today = new Date(System.currentTimeMillis());

        if (myLevel >= 5) {
            dto.setStatus("승인완료");
            dto.setApprovalStep(5);
            dto.setSign1(loginEmp.getEmpName()); dto.setSign1Date(today);
            dto.setSign2(loginEmp.getEmpName()); dto.setSign2Date(today);
            dto.setSign3(loginEmp.getEmpName()); dto.setSign3Date(today);
            dto.setSign4(loginEmp.getEmpName()); dto.setSign4Date(today);
            dto.setSign5(loginEmp.getEmpName()); dto.setSign5Date(today);
        } else {
            dto.setStatus("승인대기");
            dto.setApprovalStep(myLevel + 1);
            if (myLevel >= 1) { dto.setSign1(loginEmp.getEmpName()); dto.setSign1Date(today); }
            if (myLevel >= 2) { dto.setSign2(loginEmp.getEmpName()); dto.setSign2Date(today); }
            if (myLevel >= 3) { dto.setSign3(loginEmp.getEmpName()); dto.setSign3Date(today); }
            if (myLevel >= 4) { dto.setSign4(loginEmp.getEmpName()); dto.setSign4Date(today); }
        }

        if (dao.insertLeave(dto)) {
            response.sendRedirect("documentList.do");
        } else {
            response.getWriter().println("<script>alert('오류가 발생했습니다.'); history.back();</script>");
        }
    }
}