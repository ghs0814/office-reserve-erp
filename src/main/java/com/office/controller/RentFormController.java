package com.office.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.office.dao.EquipmentDAO;
import com.office.dto.EquipmentDTO;

/**
 * 비품 대여 신청 폼 화면으로 이동하기 전 데이터를 준비하는 컨트롤러입니다.
 */
@WebServlet("/rentForm.do")
public class RentFormController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. 목록 페이지에서 전달된 비품 번호(eqNo) 파라미터를 받습니다.[cite: 60]
        String eqNoStr = request.getParameter("eqNo");
        int eqNo = eqNoStr != null ? Integer.parseInt(eqNoStr) : 0;

        // 2. EquipmentDAO를 통해 해당 번호의 진짜 비품 정보를 DB에서 찾아옵니다.[cite: 60]
        EquipmentDAO dao = new EquipmentDAO();
        EquipmentDTO targetEq = dao.getEquipmentDetail(eqNo);

        // 3. 조회된 비품 객체(DTO)를 request 영역에 담아 전달합니다.[cite: 60]
        request.setAttribute("equipment", targetEq);
        
        // 4. 비품 대여 신청서 화면(rentForm.jsp)으로 포워딩합니다.[cite: 60]
        request.getRequestDispatcher("rentForm.jsp").forward(request, response);
    }
}