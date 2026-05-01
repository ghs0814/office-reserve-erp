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

/**
 * 신규 비품을 등록하는 컨트롤러입니다.
 */
@WebServlet("/insertEq.do")
public class EqInsertController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 인코딩 설정
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 1. 화면에서 입력한 비품명과 수량 데이터를 수집합니다.
        // 파라미터 이름을 화면과 일치하도록 totalCount로 받습니다.
        String eqName = request.getParameter("eqName");
        int totalCount = Integer.parseInt(request.getParameter("totalCount")); 

        // 2. DTO 객체를 생성하여 데이터를 포장합니다.
        EquipmentDTO dto = new EquipmentDTO();
        dto.setEqName(eqName);
        dto.setTotalCount(totalCount); 

        // 3. DAO를 통해 데이터베이스에 인서트(Insert)를 수행합니다.
        EquipmentDAO dao = new EquipmentDAO();
        boolean isSuccess = dao.insertEquipment(dto);

        // 4. 처리 결과에 따라 알림창을 띄우고 페이지를 이동시킵니다.
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