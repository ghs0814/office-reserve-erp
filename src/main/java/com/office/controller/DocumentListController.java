package com.office.controller;

import java.io.IOException;
import java.util.List;

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
 * 모든 사원이 볼 수 있는 전자 결재 기안 문서함 컨트롤러입니다.
 */
@WebServlet("/documentList.do")
public class DocumentListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        if (loginEmp == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        RentalDAO dao = new RentalDAO();
        // 방금 DAO에 만든 전체 기안 문서 조회 메서드를 호출합니다.
        List<RentalHistoryDTO> docList = dao.getAllDocumentList();

        request.setAttribute("docList", docList);
        
        // 새로 만들 documentList.jsp 로 데이터를 들고 이동합니다.
        request.getRequestDispatcher("documentList.jsp").forward(request, response);
    }
}