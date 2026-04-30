package com.office.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    // 나중에 학원에서 오라클 설치하면 연결될 주소, 아이디, 비번입니다.
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "ghs"; 
    private static final String PASSWORD = "0814";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // 오라클 드라이버 로드
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("DB 연결 성공!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}