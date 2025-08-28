FROM eclipse-temurin:21-jre-jammy
ENV TZ=Asia/Seoul LANG=C.UTF-8
RUN useradd -ms /bin/bash appuser
WORKDIR /app

ARG JAR_FILE=build/libs/*SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
RUN chown -R appuser:appuser /app
USER appuser

ENV SPRING_PROFILES_ACTIVE=prod \
    SERVER_PORT=8080 \
    JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75 -XX:+UseG1GC"

EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=5 \
  CMD wget -qO- http://localhost:${SERVER_PORT}/actuator/health | grep -q '"status":"UP"' || exit 1

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]