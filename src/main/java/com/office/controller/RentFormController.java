package com.office.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.office.dto.EquipmentDTO;

@WebServlet("/rentForm.do")
public class RentFormController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String eqNoStr = request.getParameter("eqNo");
        int eqNo = eqNoStr != null ? Integer.parseInt(eqNoStr) : 0;

        // DB 연동 전 임시 데이터 처리
        EquipmentDTO targetEq = new EquipmentDTO();
        targetEq.setEqNo(eqNo);
        targetEq.setEqName("선택하신 비품 (테스트)"); 
        targetEq.setRemainCount(5);

        /*
        // [나중에 DB 연결 시 사용할 코드]
        EquipmentDAO dao = new EquipmentDAO();
        EquipmentDTO targetEq = dao.getEquipmentDetail(eqNo);
        */

        request.setAttribute("equipment", targetEq);
        request.getRequestDispatcher("rentForm.jsp").forward(request, response);
    }
}