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
    	

    	String roomId = request.getParameter("roomId");
    	String resDate = request.getParameter("resDate");

    	ReservationDAO dao = new ReservationDAO();
    	List<String> reservedTimes = dao.getReservedTimes(roomId, resDate);

    	StringBuilder json = new StringBuilder("[");
    	for (int i = 0; i < reservedTimes.size(); i++) {
    	    json.append("\"").append(reservedTimes.get(i)).append("\"");
    	    if (i < reservedTimes.size() - 1) json.append(",");
    	}
    	json.append("]");

    	response.setContentType("application/json; charset=UTF-8");
    	PrintWriter out = response.getWriter();
    	out.print(json.toString());
    	out.flush();
    	out.close();
    }
}