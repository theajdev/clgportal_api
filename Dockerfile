FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ADD target/clgportal-api.jar clgportal-api.jar
ENTRYPOINT [ "java","-jar","/clgportal-api.jar" ]