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
 * 비품 정보를 업데이트하는 로직을 처리하는 컨트롤러입니다.
 */
@WebServlet("/updateEq.do")
public class EqUpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 인코딩 설정 및 응답 형식 지정
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 1. 수정 폼에서 넘어온 파라미터(비품번호, 이름, 총 수량, 잔여 수량)를 수집합니다.
        int eqNo = Integer.parseInt(request.getParameter("eqNo"));
        String eqName = request.getParameter("eqName");
        int totalCount = Integer.parseInt(request.getParameter("totalCount")); 
        int remainCount = Integer.parseInt(request.getParameter("remainCount")); 

        // 2. 수집된 데이터를 DTO 객체에 세팅하여 업데이트 준비를 합니다.
        EquipmentDTO dto = new EquipmentDTO();
        dto.setEqNo(eqNo);
        dto.setEqName(eqName);
        dto.setTotalCount(totalCount); 
        dto.setRemainCount(remainCount); 

        // 3. DAO를 호출하여 실제 데이터베이스의 비품 정보를 수정합니다.
        EquipmentDAO dao = new EquipmentDAO();
        boolean isSuccess = dao.updateEquipment(dto);

        // 4. 처리 결과에 따라 사용자에게 알림을 띄우고 페이지를 이동시킵니다.
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