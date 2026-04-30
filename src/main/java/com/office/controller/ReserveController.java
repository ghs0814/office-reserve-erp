package com.office.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// main.jsp에서 reserve.do?roomId=... 로 호출하면 이 서블릿이 실행됩니다.
@WebServlet("/reserve.do")
public class ReserveController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// location.href를 통한 이동은 기본적으로 GET 방식이므로 doGet을 사용합니다.
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		HttpSession session = request.getSession();
		if (session.getAttribute("loginEmp") == null) {
			response.sendRedirect("index.jsp");
			return;
		}

		// 1. main.jsp에서 넘겨준 방 번호 받기
		String roomId = request.getParameter("roomId");

		// 2. RoomDAO를 호출하여 DB에서 해당 방의 상세 정보 가져오기, 잠깐 주석
		com.office.dao.RoomDAO roomDao = new com.office.dao.RoomDAO();
		com.office.dto.RoomDTO roomInfo = roomDao.getRoomDetail(roomId);

		// 3. 만약 DB에 없는 잘못된 방 번호라면 메인으로 돌려보내기 (에러 방어)
		if (roomInfo == null) {
			System.out.println("해당 방 정보가 DB에 존재하지 않습니다: " + roomId);
			response.sendRedirect("main.jsp");
			return;
		}

		// 4. 가져온 방 상세 정보(RoomDTO 객체)를 request에 담아서 전달
		request.setAttribute("roomInfo", roomInfo);

		RequestDispatcher dispatcher = request.getRequestDispatcher("reserve.jsp");
		dispatcher.forward(request, response);
	}

	// 만약 POST로 데이터가 들어와도 doGet에서 똑같이 처리하도록 연결해 줍니다.
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}