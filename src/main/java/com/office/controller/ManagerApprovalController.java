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

@WebServlet("/managerApproval.do")
public class ManagerApprovalController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. DB 조인 결과를 흉내 낸 가짜 데이터 리스트 생성
        List<Map<String, String>> approvalList = new ArrayList<>();
        
        Map<String, String> req1 = new HashMap<>();
        req1.put("rentalNo", "1001");
        req1.put("empName", "김사원");
        req1.put("eqName", "MacBook Pro 16인치");
        req1.put("rentalDate", "2026-05-02");
        req1.put("returnDate", "2026-05-10");
        req1.put("status", "승인대기");
        approvalList.add(req1);

        Map<String, String> req2 = new HashMap<>();
        req2.put("rentalNo", "1002");
        req2.put("empName", "이대리");
        req2.put("eqName", "로지텍 무선 마우스");
        req2.put("rentalDate", "2026-05-03");
        req2.put("returnDate", "2026-05-05");
        req2.put("status", "승인대기");
        approvalList.add(req2);

        // 2. request 영역에 데이터 담기
        request.setAttribute("approvalList", approvalList);

        // 3. JSP 화면으로 포워딩
        request.getRequestDispatcher("managerApproval.jsp").forward(request, response);
    }
}