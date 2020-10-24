FROM openjdk:8-alpine
ADD target/smart-home-web-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap","-jar","/app.jar"]
