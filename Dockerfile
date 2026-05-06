# 1. 빌드 환경 (Maven과 Java 17을 사용하여 프로젝트를 .war 파일로 패키징)
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# 테스트 코드를 생략하고 패키징 진행
RUN mvn clean package -DskipTests

# 2. 실행 환경 (Tomcat 9 서버에 빌드된 war 파일 탑재)
FROM tomcat:9.0-jdk17-openjdk-slim
# Maven이 target 폴더에 생성한 프로젝트명.war 파일을 Tomcat의 ROOT(기본) 경로로 복사
# 주의: 아래 OfficeReserveERP-0.0.1-SNAPSHOT.war 부분은 pom.xml의 <artifactId>-<version>.war 이름과 동일해야 합니다.
COPY --from=build /app/target/OfficeReserveERP-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# 외부 접속 포트 개방
EXPOSE 8080

# 톰캣 서버 실행 명령어
CMD ["catalina.sh", "run"]