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
        
        // 로그인 체크 (생략 - 필요시 추가)
    	// 1. DB 연결 대신 사용할 임시 가짜 데이터(Mock Data) 생성
        List<EquipmentDTO> eqList = new ArrayList<>();
        eqList.add(new EquipmentDTO(1, "MacBook Pro 16인치 (테스트)", 5, 5));
        eqList.add(new EquipmentDTO(2, "LG Gram 15인치 (테스트)", 10, 10));
        eqList.add(new EquipmentDTO(3, "Dell 27인치 모니터 (테스트)", 20, 0)); // 재고 소진 버튼 테스트용
        eqList.add(new EquipmentDTO(4, "로지텍 무선 마우스 (테스트)", 15, 12));
        // 1. DAO를 통해 전체 비품 목록 가져오기
//        EquipmentDAO dao = new EquipmentDAO();
//        List<EquipmentDTO> eqList = dao.getAllEquipments();

        // 2. request 영역에 데이터 담기
        request.setAttribute("eqList", eqList);

        // 3. JSP 화면으로 포워딩
        request.getRequestDispatcher("equipmentList.jsp").forward(request, response);
    }
}