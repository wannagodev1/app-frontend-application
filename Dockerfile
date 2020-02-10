FROM openjdk:8-jre-alpine

MAINTAINER Wannago Dev1 <Wannago.dev1@gmail.com>

RUN apk --no-cache add curl

ENV JAVA_OPTS=""

ADD target/wannaenjoy-frontend-application.jar /app/

COPY glowroot /app/glowroot

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app/wannaenjoy-frontend-application.jar"]

HEALTHCHECK --interval=30s --timeout=30s --retries=10 CMD curl -f http://localhost:9102/actuator/health || exit 1

EXPOSE 9002 9102