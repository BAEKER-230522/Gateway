FROM gradle:7.6.1-jdk17 AS build
COPY . /home/gradle/src
WORKDIR /home/gradle/src
ARG JAR_FILE=build/libs/*.jar

FROM mcr.microsoft.com/java/jre:17-zulu-ubuntu
COPY --from=build /home/gradle/src/build/libs/*.jar /app.jar
COPY --from=build /home/gradle/src/keystore.p12 /keystore.p12
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} ${JAVA_ACTIVE} -jar /app.jar"]
