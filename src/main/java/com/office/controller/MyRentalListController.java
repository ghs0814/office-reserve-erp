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

@WebServlet("/myRentalList.do")
public class MyRentalListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

 // MyRentalListController.java의 doGet 내부
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");
        
        if (loginEmp == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        RentalDAO dao = new RentalDAO();
        // 가짜 리스트 대신 DAO를 통해 DB에서 내 대여 내역을 가져옵니다.
        // RentalDAO에 getMyRentalList(empNo) 메서드가 구현되어 있어야 합니다.
        List<RentalHistoryDTO> myList = dao.getMyRentalList(loginEmp.getEmpNo()); 

        request.setAttribute("myList", myList);
        request.getRequestDispatcher("myRentalList.jsp").forward(request, response);
    }
    
}