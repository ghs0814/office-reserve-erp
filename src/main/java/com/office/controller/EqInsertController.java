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

@WebServlet("/insertEq.do")
public class EqInsertController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 파라미터 이름을 화면과 일치하도록 totalCount로 받습니다.
        String eqName = request.getParameter("eqName");
        int totalCount = Integer.parseInt(request.getParameter("totalCount")); 

        EquipmentDTO dto = new EquipmentDTO();
        dto.setEqName(eqName);
        dto.setTotalCount(totalCount); 

        EquipmentDAO dao = new EquipmentDAO();
        boolean isSuccess = dao.insertEquipment(dto);

        PrintWriter out = response.getWriter();
        out.println("<script>");
        if (isSuccess) {
            out.println("alert('신규 비품이 성공적으로 등록되었습니다.');");
            out.println("location.href='adminEqList.do';");
        } else {
            out.println("alert('등록에 실패했습니다.');");
            out.println("history.back();");
        }
        out.println("</script>");
        out.flush();
        out.close();
    }
}