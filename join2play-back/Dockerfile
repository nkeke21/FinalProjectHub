FROM amazoncorretto:21-alpine

WORKDIR /join2play-back

COPY build/libs/*.jar join2play.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "join2play.jar"]
