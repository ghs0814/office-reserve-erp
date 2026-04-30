package com.office.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.office.dao.EquipmentDAO;
import com.office.dto.EquipmentDTO;

@WebServlet("/adminEqList.do")
public class AdminEqListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 일반 비품 목록(equipmentList.do)과 동일하게 진짜 DB 데이터를 가져옵니다.
        EquipmentDAO dao = new EquipmentDAO();
        List<EquipmentDTO> eqList = dao.getAllEquipments(); 

        // JSP에서 사용할 수 있도록 request 영역에 담기
        request.setAttribute("eqList", eqList);
        
        // adminEqList.jsp 화면으로 포워딩
        request.getRequestDispatcher("adminEqList.jsp").forward(request, response);
    }
}