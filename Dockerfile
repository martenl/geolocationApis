FROM openjdk:11
COPY target/geolocationApis-0.0.1-SNAPSHOT.jar /usr/app/app.jar
COPY src/main/resources/application.properties /usr/app/application.properties
EXPOSE 8099
ENTRYPOINT ["java","-jar","/usr/app/app.jar"]