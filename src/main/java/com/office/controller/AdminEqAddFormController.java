package com.office.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/adminEqAddForm.do")
public class AdminEqAddFormController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 비품 등록 폼 화면으로 이동
        request.getRequestDispatcher("adminEqAddForm.jsp").forward(request, response);
    }
}