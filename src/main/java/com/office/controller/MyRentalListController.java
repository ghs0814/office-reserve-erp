package com.office.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * 로그인한 사원의 개인 비품 대여 내역을 조회하는 컨트롤러입니다.
 */
@WebServlet("/myRentalList.do")
public class MyRentalListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 세션에서 로그인한 사원의 정보를 가져옵니다.[cite: 58]
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        // 로그인 정보가 없으면 초기 화면으로 리다이렉트합니다.[cite: 58]
        if (loginEmp == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        // 2. RentalDAO를 통해 로그인한 사원의 번호(empNo)로 대여 내역을 조회합니다.[cite: 58]
        RentalDAO dao = new RentalDAO();
        // RentalDAO에 구현된 getMyRentalList 메서드를 호출하여 DB 데이터를 수집합니다.[cite: 58]
        List<RentalHistoryDTO> myList = dao.getMyRentalList(loginEmp.getEmpNo()); 

        // 3. 조회된 내역을 request 영역에 저장하여 JSP로 전달할 준비를 합니다.[cite: 58]
        request.setAttribute("myList", myList);
        
        // 4. 내 대여 내역 화면(myRentalList.jsp)으로 데이터를 유지하며 이동합니다.[cite: 58]
        request.getRequestDispatcher("myRentalList.jsp").forward(request, response);
    }
}