#FROM openjdk:8-jre-slim
FROM openjdk:8-alpine
MAINTAINER xuxueli

ENV PARAMS=""

ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ADD target/xxl-job-admin-*.jar /app.jar
# jvm开启感知docker容器内存大小
ENTRYPOINT [
    "java",
    "-XX:+UnlockExperimentalVMOptions",
    "-XX:+UseCGroupMemoryLimitForHeap",
    "-jar",
    "/app.jar"
]

#ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /app.jar $PARAMS"]
