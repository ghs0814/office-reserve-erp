package com.office.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 신규 비품 등록 폼(화면) 요청 처리 컨트롤러
 * URL: /adminEqAddForm.do
 */
@WebServlet("/adminEqAddForm.do")
public class AdminEqAddFormController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 단순 화면 이동이므로 GET 방식을 사용합니다.
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 데이터 처리 없이 바로 비품 등록 폼(adminEqAddForm.jsp) 화면으로 요청을 넘깁니다(Forward).
        request.getRequestDispatcher("adminEqAddForm.jsp").forward(request, response);
    }
}