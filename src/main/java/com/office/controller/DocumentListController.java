package com.office.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.office.dao.RentalDAO;
import com.office.dao.LeaveDAO; // 추가
import com.office.dto.EmployeeDTO;
import com.office.dto.RentalHistoryDTO;
import com.office.dto.LeaveHistoryDTO; // 추가

@WebServlet("/documentList.do")
public class DocumentListController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        if (loginEmp == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // 1. 비품 대여 기안 목록 가져오기
        RentalDAO rentalDao = new RentalDAO();
        List<RentalHistoryDTO> docList = rentalDao.getAllDocumentList();
        request.setAttribute("docList", docList);

        // 2. 휴가 신청 기안 목록 가져오기 (LeaveDAO에 구현 필요)
        LeaveDAO leaveDao = new LeaveDAO();
        // 전체 휴가 기안을 가져오는 메서드 (아래 DAO 수정 참고)
        List<LeaveHistoryDTO> leaveDocList = leaveDao.getAllLeaveDocuments(); 
        request.setAttribute("leaveDocList", leaveDocList);
        
        request.getRequestDispatcher("documentList.jsp").forward(request, response);
    }
}