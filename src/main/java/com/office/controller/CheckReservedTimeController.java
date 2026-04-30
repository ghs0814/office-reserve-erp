package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.office.dao.ReservationDAO;

@WebServlet("/checkReservedTime.do")
public class CheckReservedTimeController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String roomId = request.getParameter("roomId");
//        String resDate = request.getParameter("resDate");
//
//        ReservationDAO dao = new ReservationDAO();
//        List<String> reservedTimes = dao.getReservedTimes(roomId, resDate);
//
//        
//        
//        // JSON 형태로 변환 (라이브러리 없이 간단하게 구현)
//        // 예: ["09:00", "13:00"]
//        StringBuilder json = new StringBuilder("[");
//        for (int i = 0; i < reservedTimes.size(); i++) {
//            json.append("\"").append(reservedTimes.get(i)).append("\"");
//            if (i < reservedTimes.size() - 1) json.append(",");
//        }
//        json.append("]");
//
//        response.setContentType("application/json; charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        out.print(json.toString());
//        out.flush();
    	// 1. 파라미터는 일단 받지만 사용하지 않습니다.
        String roomId = request.getParameter("roomId");
        String resDate = request.getParameter("resDate");

        // 2. DB 대신 가짜 데이터(Mock Data) 생성
        // 특정 날짜를 고르면 무조건 10시와 14시가 예약된 것으로 시뮬레이션합니다.
        String mockJson = "[\"10:00\", \"14:00\"]";

        // 3. JSON 응답 보내기
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(mockJson);
        out.flush();
        out.close();
    }
}