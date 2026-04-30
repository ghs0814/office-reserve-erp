package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.office.dao.EquipmentDAO;

@WebServlet("/deleteEq.do")
public class EqDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        int eqNo = Integer.parseInt(request.getParameter("eqNo"));
        EquipmentDAO dao = new EquipmentDAO();

        PrintWriter out = response.getWriter();
        out.println("<script>");
        try {
            boolean isSuccess = dao.deleteEquipment(eqNo);
            if (isSuccess) {
                out.println("alert('비품이 성공적으로 폐기되었습니다.');");
                out.println("location.href='adminEqList.do';");
            } else {
                out.println("alert('폐기 처리에 실패했습니다.');");
                out.println("history.back();");
            }
        } catch (Exception e) {
            // 해당 비품이 대여 내역 테이블에 묶여있어 외래 키 오류가 발생하는 경우
            out.println("alert('현재 대여 기록이 남아있는 비품은 폐기할 수 없습니다.');");
            out.println("history.back();");
        }
        out.println("</script>");
        out.flush();
        out.close();
    }
}