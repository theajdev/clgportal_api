FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ADD target/*.jar clgportal-api.jar
ENTRYPOINT [ "java","-jar","/clgportal-api.jar" ]