package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/adminEqAddProcess.do")
public class AdminEqAddProcessController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 1. 화면에서 넘어온 데이터 받기
        String eqName = request.getParameter("eqName");
        String totalCountStr = request.getParameter("totalCount");

        PrintWriter out = response.getWriter();
        out.println("<script>");

        if (eqName != null && totalCountStr != null) {
            /*
            // [DB 연결 시 사용할 로직]
            int totalCount = Integer.parseInt(totalCountStr);
            EquipmentDTO dto = new EquipmentDTO();
            dto.setEqName(eqName);
            dto.setTotalCount(totalCount);
            dto.setRemainCount(totalCount); // 처음 등록 시 총 수량 = 남은 수량
            
            EquipmentDAO dao = new EquipmentDAO();
            dao.insertEquipment(dto);
            */

            out.println("alert('[" + eqName + "] " + totalCountStr + "개가 성공적으로 등록되었습니다.');");
            out.println("location.href='" + request.getContextPath() + "/adminEqList.do';"); 
        } else {
            out.println("alert('입력값이 올바르지 않습니다.');");
            out.println("history.back();");
        }
        
        out.println("</script>");
        out.flush();
        out.close();
    }
}