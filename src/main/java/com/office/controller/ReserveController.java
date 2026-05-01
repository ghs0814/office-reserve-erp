package com.office.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 회의실 예약 페이지로 진입하기 전 해당 회의실의 정보를 조회하는 컨트롤러입니다.
 */
@WebServlet("/reserve.do")
public class ReserveController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
     * 메인 지도의 링크 클릭(GET 방식) 시 동작합니다.
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		// 1. 보안을 위해 현재 로그인 상태인지 세션을 먼저 체크합니다.
		HttpSession session = request.getSession();
		if (session.getAttribute("loginEmp") == null) {
			response.sendRedirect("index.jsp");
			return;
		}

		// 2. 메인 페이지(main.jsp)에서 전달해준 회의실 번호(roomId)를 수집합니다.
		String roomId = request.getParameter("roomId");

		// 3. RoomDAO를 통해 데이터베이스에서 해당 회의실의 상세 정보를 가져옵니다.
		com.office.dao.RoomDAO roomDao = new com.office.dao.RoomDAO();
		com.office.dto.RoomDTO roomInfo = roomDao.getRoomDetail(roomId);

		// 4. 예외 방어: 만약 DB에 없는 방 번호가 전달되었다면 메인으로 다시 보냅니다.
		if (roomInfo == null) {
			System.out.println("해당 방 정보가 DB에 존재하지 않습니다: " + roomId);
			response.sendRedirect("main.jsp");
			return;
		}

		// 5. 조회된 회의실 정보(DTO)를 request에 담아 예약 신청 화면(reserve.jsp)으로 전달합니다.
		request.setAttribute("roomInfo", roomInfo);

		RequestDispatcher dispatcher = request.getRequestDispatcher("reserve.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}