FROM openjdk:8-jre
WORKDIR /app
ADD blog-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar"]
CMD ["app.jar"]