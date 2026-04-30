package com.office.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.office.dto.RentalHistoryDTO;

@WebServlet("/managerApproval.do")
public class ManagerApprovalController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // [테스트용 가짜 데이터 생성] 
        // 나중에는 RentalDAO.getPendingList(현재관리자등급) 으로 교체됩니다.
        List<RentalHistoryDTO> mockList = new ArrayList<>();
        
        // 1단계 대기 중인 기안
        RentalHistoryDTO dto1 = new RentalHistoryDTO(101, 202601, 5, Date.valueOf("2026-05-01"), Date.valueOf("2026-05-05"), "승인대기", 1, null, null, null, null, null);
        
        // 2단계 대기 중인 기안 (1단계 승인 완료 상태)
        RentalHistoryDTO dto2 = new RentalHistoryDTO(102, 202602, 3, Date.valueOf("2026-05-10"), Date.valueOf("2026-05-12"), "승인대기", 2, "김팀장", null, null, null, null);

        // 4단계 대기 중인 기안 (1, 2, 3단계 승인 완료 상태)
        RentalHistoryDTO dto3 = new RentalHistoryDTO(103, 202603, 1, Date.valueOf("2026-05-20"), Date.valueOf("2026-05-25"), "승인대기", 4, "김팀장", "이부장", "박본부장", null, null);

        mockList.add(dto1);
        mockList.add(dto2);
        mockList.add(dto3);

        request.setAttribute("approvalList", mockList);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("managerApproval.jsp");
        dispatcher.forward(request, response);
    }
}