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

@WebServlet("/myRentalList.do")
public class MyRentalListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 로그인한 사용자 정보 가져오기 (세션)
        // EmployeeDTO loginEmp = (EmployeeDTO) request.getSession().getAttribute("loginEmp");
        
        // 1. 가짜 데이터(Mock Data) 생성: 내 대여 내역 3가지 상태 시뮬레이션
        List<Map<String, String>> myList = new ArrayList<>();
        
        Map<String, String> item1 = new HashMap<>();
        item1.put("rentalNo", "1001");
        item1.put("eqName", "MacBook Pro 16인치");
        item1.put("rentalDate", "2026-05-02");
        item1.put("returnDate", "2026-05-10");
        item1.put("status", "승인대기"); // 아직 관리자 확인 전
        myList.add(item1);

        Map<String, String> item2 = new HashMap<>();
        item2.put("rentalNo", "998");
        item2.put("eqName", "로지텍 무선 마우스");
        item2.put("rentalDate", "2026-04-15");
        item2.put("returnDate", "2026-04-20");
        item2.put("status", "대여중"); // 관리자 승인 완료
        myList.add(item2);
        
        Map<String, String> item3 = new HashMap<>();
        item3.put("rentalNo", "950");
        item3.put("eqName", "Dell 27인치 모니터");
        item3.put("rentalDate", "2026-03-01");
        item3.put("returnDate", "2026-03-05");
        item3.put("status", "반납완료"); // 사용 끝남
        myList.add(item3);

        // 2. request 영역에 데이터 담기
        request.setAttribute("myList", myList);

        // 3. JSP 화면으로 포워딩
        request.getRequestDispatcher("myRentalList.jsp").forward(request, response);
    }
}