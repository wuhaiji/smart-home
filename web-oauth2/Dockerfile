FROM openjdk:8-jre-slim
ADD target/web-oauth2-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap","-jar","/app.jar"]
