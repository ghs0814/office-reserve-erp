package com.office.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // 오라클 드라이버 로드
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
            // 나중에 학원에서 오라클 설치하면 주소, 아이디, 비번을 여기에 넣습니다.
            String url = "jdbc:oracle:thin:@localhost:1521:xe";
            String user = "ghs"; 
            String password = "0814";
            
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("DB 연결 성공!");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}