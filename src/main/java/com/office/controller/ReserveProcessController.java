package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.office.dao.ReservationDAO;
import com.office.dto.ReservationDTO;

@WebServlet("/reserveProcess.do")
public class ReserveProcessController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. 한글 깨짐 방지
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 2. 화면에서 넘어온 데이터 받기 (자료형 변환 포함)
        String roomId = request.getParameter("roomId");
        int empNo = Integer.parseInt(request.getParameter("empNo"));
        Date resDate = Date.valueOf(request.getParameter("resDate"));
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String purpose = request.getParameter("purpose");

        ReservationDAO dao = new ReservationDAO();
        PrintWriter out = response.getWriter();
        out.println("<script>");

        // 3. 예약 중복 검사 (DAO의 checkDuplicate 메서드 활용)
        boolean isDuplicate = dao.checkDuplicate(roomId, resDate, startTime);

        if (isDuplicate) {
            // 이미 예약된 경우 진행 중단 및 뒤로가기
            out.println("alert('이미 해당 시간에 예약이 존재합니다. 다른 시간을 선택해주세요.');");
            out.println("history.back();");
        } else {
            // 4. 중복이 아닐 경우 DTO 객체에 담기
            ReservationDTO dto = new ReservationDTO();
            dto.setRoomId(roomId);
            dto.setEmpNo(empNo);
            dto.setResDate(resDate);
            dto.setStartTime(startTime);
            dto.setEndTime(endTime);
            dto.setPurpose(purpose);

            // 5. DAO를 호출하여 DB에 INSERT 실행
            boolean isSuccess = dao.insertReservation(dto);

            // 6. 결과에 따른 알림 및 화면 이동
            if (isSuccess) {
                out.println("alert('회의실 예약이 성공적으로 완료되었습니다.');");
                out.println("location.href='main.jsp';"); 
            } else {
                out.println("alert('예약 처리 중 DB 오류가 발생했습니다.');");
                out.println("history.back();");
            }
        }
        
        out.println("</script>");
        out.flush();
        out.close();
    }
}