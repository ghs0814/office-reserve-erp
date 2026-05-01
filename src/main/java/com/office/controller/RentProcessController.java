package com.office.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.office.dao.RentalDAO;
import com.office.dto.RentalHistoryDTO;

/**
 * 사용자가 작성한 비품 대여 신청을 처리하고 결재를 생성하는 컨트롤러입니다.
 */
@WebServlet("/rentProcess.do")
public class RentProcessController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 한글 깨짐 방지 및 응답 형식 설정
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 1. 신청 폼에서 넘어온 데이터(비품번호, 사번, 대여/반납일)를 수집합니다.
        String eqNoStr = request.getParameter("eqNo");
        String empNoStr = request.getParameter("empNo");
        String rentalDateStr = request.getParameter("rentalDate");
        String returnDateStr = request.getParameter("returnDate");

        PrintWriter out = response.getWriter();
        out.println("<script>");

        try {
            // 2. 수집된 문자열 데이터를 숫자 및 SQL 날짜 타입으로 변환합니다.
            int eqNo = Integer.parseInt(eqNoStr);
            int empNo = Integer.parseInt(empNoStr);
            Date rentalDate = Date.valueOf(rentalDateStr);
            Date returnDate = Date.valueOf(returnDateStr);

            // 3. 세션에서 현재 로그인한 사원(기안자) 정보를 가져옵니다.
            javax.servlet.http.HttpSession session = request.getSession();
            com.office.dto.EmployeeDTO loginEmp = (com.office.dto.EmployeeDTO) session.getAttribute("loginEmp");

            // 4. DTO 객체에 대여 정보를 세팅합니다.
            RentalHistoryDTO dto = new RentalHistoryDTO();
            dto.setEqNo(eqNo);
            dto.setEmpNo(empNo);
            dto.setRentalDate(rentalDate);
            dto.setReturnDate(returnDate);
            
            // [핵심 로직] 기안자는 1단계 승인자로 간주하여 Sign1에 이름을 넣고, 
            // 현재 결재 단계를 2단계(다음 결재자 대기)로 설정하여 상신합니다.
            // dto.setApprovalStep(2); 
            // dto.setSign1(loginEmp.getEmpName());
            
         // [수정 후] 신청자 등급(empLevel)에 따른 동적 결재 라인 설정
            int myLevel = loginEmp.getEmpLevel(); // 현재 로그인한 사원의 등급[cite: 28]

            if (myLevel >= 5) {
                // 5등급(최고 관리자): 즉시 승인 완료 처리
                dto.setStatus("대여중");
                dto.setApprovalStep(5);
                dto.setSign1(loginEmp.getEmpName());
                dto.setSign2(loginEmp.getEmpName());
                dto.setSign3(loginEmp.getEmpName());
                dto.setSign4(loginEmp.getEmpName());
                dto.setSign5(loginEmp.getEmpName());
            } else {
                // 1~4등급: 본인 등급 다음 단계부터 결재 시작
                dto.setStatus("승인대기");
                dto.setApprovalStep(myLevel + 1);
                
                // 본인 등급까지는 자동으로 서명 처리
                if (myLevel >= 1) dto.setSign1(loginEmp.getEmpName());
                if (myLevel >= 2) dto.setSign2(loginEmp.getEmpName());
                if (myLevel >= 3) dto.setSign3(loginEmp.getEmpName());
                if (myLevel >= 4) dto.setSign4(loginEmp.getEmpName());
            }
            
            // 5. RentalDAO를 통해 DB에 대여 기안 내역을 저장(Insert)합니다.
            RentalDAO dao = new RentalDAO();
            boolean isSuccess = dao.insertRental(dto);

            // 6. 저장 결과에 따른 화면 이동 처리
            if (isSuccess) {
                out.println("alert('비품 대여 결재가 성공적으로 기안되었습니다. (관리자 승인 대기 중)');");
                // 성공 시 사용자의 대여 내역 리스트 페이지로 이동합니다.
                out.println("location.href='myRentalList.do';"); 
            } else {
                out.println("alert('결재 기안 중 오류가 발생했습니다.');");
                out.println("history.back();");
            }

        } catch (Exception e) {
            // 데이터 변환 오류(날짜 형식 등) 발생 시 예외 처리
            e.printStackTrace();
            out.println("alert('데이터 처리 중 오류가 발생했습니다. 날짜 형식을 확인해주세요.');");
            out.println("history.back();");
        }
        
        out.println("</script>");
        out.flush();
        out.close();
    }
}