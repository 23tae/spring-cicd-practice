# 베이스 이미지로 Java 17 버전을 사용
FROM openjdk:17-jdk-slim

# jar 파일이 생성될 경로를 변수로 지정
ARG JAR_FILE=build/libs/*.jar

# jar 파일을 이미지 안의 app.jar로 복사
COPY ${JAR_FILE} app.jar

# 애플리케이션 실행 명령어
ENTRYPOINT ["java","-jar","/app.jar"]