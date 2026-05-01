package com.office.util;

import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 데이터베이스 커넥션 풀을 관리하는 유틸리티 클래스입니다.
 */
public class DBConnection {
	// 1. 히카리 데이터소스 객체 생성 (프로그램 전체에서 하나만 공유하기 위해 static 선언)
	private static HikariDataSource ds;

	// 2. static 블록: 클래스가 메모리에 처음 로드될 때 단 한 번만 실행되어 커넥션 풀을 초기화합니다.
	static {
		try {
			HikariConfig config = new HikariConfig();

			// 오라클 DB 연결 정보 (필요 시 주석 해제 후 사용)
			// config.setDriverClassName("oracle.jdbc.driver.OracleDriver");
			// config.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:xe");
			// config.setUsername("ghs");
			// config.setPassword("0814");

			// 현재 활성화된 설정: H2 데이터베이스 연결 정보
			config.setDriverClassName("org.h2.Driver");
			config.setJdbcUrl("jdbc:h2:~/office_db;MODE=Oracle;AUTO_SERVER=TRUE");
			config.setUsername("sa");
			config.setPassword(""); // H2 기본 비밀번호는 공백입니다.

			// 히카리CP 세부 옵션 설정
			config.setMaximumPoolSize(10); // 동시에 유지할 최대 커넥션 개수
			config.setMinimumIdle(5); // 대기 상태로 유지할 최소 커넥션 개수
			config.setConnectionTimeout(30000); // 연결 시도 시 최대 대기 시간 (30초)

			// 설정 정보를 바탕으로 데이터소스 객체 생성
			ds = new HikariDataSource(config);
			System.out.println("HikariCP 커넥션 풀 생성 완료!");

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("HikariCP 초기화 실패");
		}
	}

	/**
	 * 커넥션 풀에서 유효한 커넥션 하나를 빌려와 반환합니다.
	 */
	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
}