package com.office.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.office.dao.RentalDAO;
import com.office.dto.EmployeeDTO;
import com.office.dto.RentalHistoryDTO;

/**
 * 관리자별 권한 등급에 맞는 결재 대기 목록을 조회하는 컨트롤러입니다.
 */
@WebServlet("/managerApproval.do")
public class ManagerApprovalController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. 세션에서 현재 로그인한 사용자의 정보를 확인합니다.
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        // 로그인 정보가 없으면 로그인 페이지로 튕겨냅니다. (보안 처리)
        if (loginEmp == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // 2. 로그인한 사용자의 권한 레벨(empLevel)을 추출합니다.
        int managerLevel = loginEmp.getEmpLevel();

        // 3. RentalDAO를 호출하여 현재 관리자 등급에서 결재해야 할 목록만 DB에서 가져옵니다.
        RentalDAO dao = new RentalDAO();
        List<RentalHistoryDTO> approvalList = dao.getPendingList(managerLevel);

        // 4. 조회된 리스트를 request 영역에 담아 JSP 화면으로 전달합니다.
        request.setAttribute("approvalList", approvalList);
        
        // 5. 관리자 전용 결재함 화면(managerApproval.jsp)으로 포워딩합니다.
        RequestDispatcher dispatcher = request.getRequestDispatcher("managerApproval.jsp");
        dispatcher.forward(request, response);
    }
}