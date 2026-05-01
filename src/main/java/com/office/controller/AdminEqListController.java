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

/**
 * 관리자용 비품 목록 조회 및 관리 대시보드 컨트롤러
 */
@WebServlet("/adminEqList.do")
public class AdminEqListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. EquipmentDAO를 통해 DB에서 전체 비품 데이터를 가져옵니다.
        EquipmentDAO dao = new EquipmentDAO();
        List<EquipmentDTO> eqList = dao.getAllEquipments(); 

        // 2. JSP 화면에서 리스트를 출력할 수 있도록 request 객체에 데이터를 담습니다.
        request.setAttribute("eqList", eqList);
        
        // 3. 관리자용 비품 목록 화면(adminEqList.jsp)으로 포워딩(데이터 유지 이동)합니다.
        request.getRequestDispatcher("adminEqList.jsp").forward(request, response);
    }
}