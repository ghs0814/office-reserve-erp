package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.office.dao.EmployeeDAO;
import com.office.dto.EmployeeDTO;

/**
 * 관리자 페이지에서 발생하는 액션(직급변경, 권한이양, 퇴사)을 처리하는 컨트롤러입니다.
 */
@WebServlet("/adminAction.do")
public class AdminActionController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        EmployeeDTO loginEmp = (EmployeeDTO) session.getAttribute("loginEmp");

        // 관리자가 아니면 튕겨내는 방어 로직
        if (loginEmp == null || !"Y".equals(loginEmp.getManager())) {
            out.println("<script>alert('잘못된 접근입니다.'); location.href='index.jsp';</script>");
            return;
        }

        String action = request.getParameter("action");
        int targetEmpNo = Integer.parseInt(request.getParameter("empNo"));
        EmployeeDAO dao = new EmployeeDAO();
        boolean isSuccess = false;

        out.println("<script>");

        try {
            if ("updateLevel".equals(action)) {
                // 직급 변경 로직
                int newLevel = Integer.parseInt(request.getParameter("newLevel"));
                isSuccess = dao.updateEmployeeLevel(targetEmpNo, newLevel);
                
                if (isSuccess) {
                    out.println("alert('직급이 성공적으로 변경되었습니다.');");
                    out.println("location.href='admin.do';");
                }
                
            } else if ("transferManager".equals(action)) {
                // 권한 이양 로직
                isSuccess = dao.transferManagerRole(loginEmp.getEmpNo(), targetEmpNo);
                
                if (isSuccess) {
                    // 권한을 넘겼으므로 세션을 초기화하고 강제 로그아웃 처리합니다.
                    session.invalidate();
                    out.println("alert('관리자 권한을 성공적으로 넘겼습니다. 일반 사원 권한으로 변경되어 로그아웃됩니다.');");
                    out.println("location.href='index.jsp';");
                }
                
            } else if ("deleteEmp".equals(action)) {
                // 퇴사(삭제) 처리 로직
                isSuccess = dao.deleteEmployee(targetEmpNo);
                
                if (isSuccess) {
                    out.println("alert('해당 사원의 퇴사(삭제) 처리가 완료되었습니다.');");
                    out.println("location.href='admin.do';");
                }
            }

            if (!isSuccess) {
                out.println("alert('요청하신 작업 처리에 실패했습니다.');");
                out.println("history.back();");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("alert('서버 오류가 발생했습니다.');");
            out.println("history.back();");
        }

        out.println("</script>");
        out.flush();
        out.close();
    }
}