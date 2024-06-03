# jdk 설치
FROM eclipse-temurin:21

# 소스코드 가져오기
WORKDIR /app
COPY . .

# 소스코드 빌드
RUN <<EOF
./gradlew bootJar
mv build/libs/*.jar app.jar
EOF

## 여기부터 새로운 Stage가 시작된다.
#FROM eclipse-temurin:21-jre
#
#WORKDIR /app
## COPY를 하되, build 단계에서 만든 app.jar만 가져온다.
#COPY --from=build /app/app.jar .

CMD ["java", "-jar", "app.jar"]
EXPOSE 8080