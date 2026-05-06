package com.office.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.stream.Collectors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DBInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("=== 톰캣 서버 구동: H2 DB 초기화 확인 시작 ===");
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 1. EMPLOYEE 테이블이 존재하는지 확인 (DB가 비어있는지 체크)
            boolean tableExists = false;
            try (ResultSet rs = conn.getMetaData().getTables(null, null, "EMPLOYEE", null)) {
                if (rs.next()) {
                    tableExists = true;
                }
            }

            // 2. 테이블이 없다면 (Render가 Sleep 후 깨어나 DB가 초기화된 상태라면)
            if (!tableExists) {
                System.out.println("-> DB가 비어있음을 감지했습니다. 초기 스크립트를 실행합니다.");
                
                // 3. WEB-INF/sql/init.sql 파일을 읽어옵니다.
                InputStream is = sce.getServletContext().getResourceAsStream("/WEB-INF/sql/init.sql");
                if (is != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String sqlScript = reader.lines().collect(Collectors.joining("\n"));
                    
                    // 4. 세미콜론(;) 단위로 쿼리를 쪼개어 실행합니다.
                    String[] queries = sqlScript.split(";");
                    for (String query : queries) {
                        if (!query.trim().isEmpty()) {
                            stmt.execute(query.trim());
                        }
                    }
                    System.out.println("-> DB 초기 데이터 세팅이 완벽하게 끝났습니다!");
                } else {
                    System.err.println("-> init.sql 파일을 찾을 수 없습니다.");
                }
            } else {
                System.out.println("-> 기존 DB 데이터가 보존되어 있습니다. (세팅 패스)");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("-> DB 초기화 중 에러가 발생했습니다.");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 서버 종료 시 특별히 할 작업이 없다면 비워둡니다.
        System.out.println("=== 톰캣 서버 종료 ===");
    }
}