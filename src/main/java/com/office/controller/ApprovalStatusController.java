package com.office.controller;

import java.io.IOException;
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
 * 기안 제목 또는 상태를 클릭했을 때 상세 내역 및 결재 화면을 보여주는 컨트롤러입니다.
 */
@WebServlet(urlPatterns = {"/approvalStatus.do", "/documentDetail.do"})
public class ApprovalStatusController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        if (loginEmp == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        int rentalNo = Integer.parseInt(request.getParameter("rentalNo"));
        
        RentalDAO dao = new RentalDAO();
        RentalHistoryDTO docDetail = dao.getDocumentDetail(rentalNo);
        
        // 이중 보안 체크
        if (docDetail != null) {
            if (loginEmp.getEmpNo() != docDetail.getEmpNo() && loginEmp.getEmpLevel() <= docDetail.getEmpLevel()) {
                response.setContentType("text/html; charset=UTF-8");
                response.getWriter().println("<script>alert('해당 문서를 열람할 권한이 없습니다.'); history.back();</script>");
                return;
            }
        }

        // [핵심 포인트] 화면을 이동시키기 전에 반드시 데이터를 request에 먼저 담아야 합니다.
        request.setAttribute("docDetail", docDetail);

        // URL을 확인하여 화면 분기 처리
        String uri = request.getRequestURI();
        if (uri.contains("documentDetail.do")) {
            // 기안 제목 클릭 시: 원본 폼 화면으로 이동
            request.getRequestDispatcher("documentDetail.jsp").forward(request, response);
        } else {
            // 상태 뱃지 클릭 시: 결재 라인이 포함된 3분할 화면으로 이동
            request.getRequestDispatcher("approvalStatus.jsp").forward(request, response);
        }
    }
}