package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.office.dao.EquipmentDAO;
import com.office.dto.EquipmentDTO;

@WebServlet("/updateEq.do")
public class EqUpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 파라미터 이름을 화면과 일치하도록 totalCount, remainCount로 받습니다.
        int eqNo = Integer.parseInt(request.getParameter("eqNo"));
        String eqName = request.getParameter("eqName");
        int totalCount = Integer.parseInt(request.getParameter("totalCount")); 
        int remainCount = Integer.parseInt(request.getParameter("remainCount")); 

        EquipmentDTO dto = new EquipmentDTO();
        dto.setEqNo(eqNo);
        dto.setEqName(eqName);
        dto.setTotalCount(totalCount); 
        dto.setRemainCount(remainCount); 

        EquipmentDAO dao = new EquipmentDAO();
        boolean isSuccess = dao.updateEquipment(dto);

        PrintWriter out = response.getWriter();
        out.println("<script>");
        if (isSuccess) {
            out.println("alert('비품 정보가 성공적으로 수정되었습니다.');");
            out.println("location.href='adminEqList.do';");
        } else {
            out.println("alert('수정에 실패했습니다.');");
            out.println("history.back();");
        }
        out.println("</script>");
        out.flush();
        out.close();
    }
}