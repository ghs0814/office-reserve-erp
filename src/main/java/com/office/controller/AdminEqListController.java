package com.office.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.office.dto.EquipmentDTO;

@WebServlet("/adminEqList.do")
public class AdminEqListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. 가짜 데이터 생성 (관리자용 전체 비품 현황)
        List<EquipmentDTO> eqList = new ArrayList<>();
        eqList.add(new EquipmentDTO(1, "MacBook Pro 16인치", 5, 2));
        eqList.add(new EquipmentDTO(2, "LG Gram 15인치", 10, 8));
        eqList.add(new EquipmentDTO(3, "Dell 27인치 모니터", 20, 20));
        eqList.add(new EquipmentDTO(4, "로지텍 무선 마우스", 15, 0));

        // 2. request 영역에 데이터 담기
        request.setAttribute("eqList", eqList);

        // 3. 관리자 전용 JSP 화면으로 포워딩
        request.getRequestDispatcher("adminEqList.jsp").forward(request, response);
    }
}