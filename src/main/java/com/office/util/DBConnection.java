package com.office.util;

import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBConnection {
    // 1. 히카리 데이터소스 객체 생성 (싱글톤 패턴처럼 하나만 유지)
    private static HikariDataSource ds;

    // 2. static 블록: 클래스가 메모리에 올라갈 때 딱 한 번만 풀(Pool)을 생성함
    static {
        try {
            HikariConfig config = new HikariConfig();
            
            // 오라클 DB 연결 정보 설정
//            config.setDriverClassName("oracle.jdbc.driver.OracleDriver");
//            config.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:xe");
//            config.setUsername("ghs");
//            config.setPassword("0814");
            config.setDriverClassName("org.h2.Driver");
            config.setJdbcUrl("jdbc:h2:~/office_db;MODE=Oracle;AUTO_SERVER=TRUE");
            config.setUsername("sa");
            config.setPassword(""); // H2 기본 비밀번호는 없습니다.
            // 히카리CP 옵션 설정 (선택 사항이지만 실무에서 자주 쓰는 세팅)
            config.setMaximumPoolSize(10); // 최대 만들어둘 커넥션 개수
            config.setMinimumIdle(5);      // 최소한으로 유지할 커넥션 개수
            config.setConnectionTimeout(30000); // 연결 대기 시간 (30초)
            
            ds = new HikariDataSource(config);
            System.out.println("HikariCP 커넥션 풀 생성 완료!");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("HikariCP 초기화 실패");
        }
    }

    // 3. 커넥션 풀에서 커넥션을 하나씩 빌려오는 메서드
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}