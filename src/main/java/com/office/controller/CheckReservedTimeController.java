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

/**
 * 회의실 예약 중복 방지를 위해 이미 예약된 시간 목록을 AJAX로 제공하는 컨트롤러입니다.
 */
@WebServlet("/checkReservedTime.do")
public class CheckReservedTimeController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        // 1. 요청으로부터 회의실 ID와 선택된 예약 날짜 파라미터를 가져옵니다.
    	String roomId = request.getParameter("roomId");
    	String resDate = request.getParameter("resDate");

        // 2. DAO를 통해 해당 날짜와 회의실에 예약된 시간 리스트를 조회합니다.
    	ReservationDAO dao = new ReservationDAO();
    	List<String> reservedTimes = dao.getReservedTimes(roomId, resDate);

        // 3. 자바 리스트 데이터를 JSON 배열 형태의 문자열로 직접 조립합니다.
    	StringBuilder json = new StringBuilder("[");
    	for (int i = 0; i < reservedTimes.size(); i++) {
    	    json.append("\"").append(reservedTimes.get(i)).append("\"");
    	    if (i < reservedTimes.size() - 1) json.append(",");
    	}
    	json.append("]");

        // 4. 응답 형식을 JSON으로 설정하고 조립된 문자열을 브라우저로 전송합니다.
    	response.setContentType("application/json; charset=UTF-8");
    	PrintWriter out = response.getWriter();
    	out.print(json.toString());
    	out.flush();
    	out.close();
    }
}