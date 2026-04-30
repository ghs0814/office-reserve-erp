package com.office.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.office.dao.EquipmentDAO;
import com.office.dto.EquipmentDTO;

@WebServlet("/equipmentList.do")
public class EquipmentListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	// 1. DAO를 통해 전체 비품 목록 가져오기
    	EquipmentDAO dao = new EquipmentDAO();
    	List<EquipmentDTO> eqList = dao.getAllEquipments();

    	// 2. request 영역에 데이터 담기
    	request.setAttribute("eqList", eqList);

    	// 3. JSP 화면으로 포워딩
    	request.getRequestDispatcher("equipmentList.jsp").forward(request, response);
    }
}