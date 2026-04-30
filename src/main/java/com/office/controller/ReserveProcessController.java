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
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 1. 파라미터 수집
        String roomId = request.getParameter("roomId");
        int empNo = Integer.parseInt(request.getParameter("empNo"));
        
        // 날짜 변환 시 예외 방지 (간단한 처리)
        Date resDate = null;
        try {
            resDate = Date.valueOf(request.getParameter("resDate"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String purpose = request.getParameter("purpose");

        ReservationDAO dao = new ReservationDAO();
        PrintWriter out = response.getWriter();
        out.println("<script>");

        // 2. 중복 검사
        boolean isDuplicate = dao.checkDuplicate(roomId, resDate, startTime);

        if (isDuplicate) {
            out.println("alert('이미 해당 시간에 예약이 존재합니다. 다른 시간을 선택해주세요.');");
            out.println("history.back();");
        } else {
            // 3. DTO 객체 생성 및 데이터 세팅
            ReservationDTO dto = new ReservationDTO();
            dto.setRoomId(roomId);
            dto.setEmpNo(empNo);
            dto.setResDate(resDate);
            dto.setStartTime(startTime);
            dto.setEndTime(endTime);
            dto.setPurpose(purpose);

            // 4. DB INSERT 실행
            boolean isSuccess = dao.insertReservation(dto);

            if (isSuccess) {
                out.println("alert('회의실 예약이 성공적으로 완료되었습니다.');");
                // 예약 완료 후 목록 페이지로 이동하는 것이 일반적입니다.
                out.println("location.href='myReserveList.do';"); 
            } else {
                out.println("alert('예약 처리 중 오류가 발생했습니다.');");
                out.println("history.back();");
            }
        }
        
        out.println("</script>");
        out.flush();
        out.close();
    }
}