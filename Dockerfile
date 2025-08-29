# ---------- Build stage ----------
FROM gradle:8.8-jdk21-alpine AS build
WORKDIR /workspace
COPY . .
# 캐시 활용
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar -x test

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# 타임존(서울)
RUN apk add --no-cache tzdata && ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
# 로그 디렉토리 생성 및 권한 설정
RUN mkdir -p /app/log && chown -R spring:spring /app/log
# 보안: nobody 유저
RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=build /workspace/build/libs/*.jar app.jar
USER spring
EXPOSE 8080
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75","-Duser.timezone=Asia/Seoul","-jar","/app/app.jar"]
