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

/**
 * 전체 비품 목록을 조회하여 화면에 전달하는 컨트롤러입니다.
 */
@WebServlet("/equipmentList.do")
public class EquipmentListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	// 1. DAO를 통해 데이터베이스로부터 전체 비품 목록을 가져옵니다.
    	EquipmentDAO dao = new EquipmentDAO();
    	List<EquipmentDTO> eqList = dao.getAllEquipments();

    	// 2. JSP 화면에서 리스트를 출력할 수 있도록 request 영역에 데이터를 담습니다.
    	request.setAttribute("eqList", eqList);

    	// 3. 비품 목록 화면(equipmentList.jsp)으로 포워딩하여 이동합니다.
    	request.getRequestDispatcher("equipmentList.jsp").forward(request, response);
    }
}