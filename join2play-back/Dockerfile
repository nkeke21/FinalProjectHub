FROM amazoncorretto:21

WORKDIR /join2play-back

COPY join2play/build/libs/*.jar join2play.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "join2play.jar"]
