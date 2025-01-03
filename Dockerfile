FROM openjdk:17
EXPOSE 2025
ADD target/clgportal-api.jar clgportal-api.jar
ENTRYPOINT [ "java","-jar","/clgportal-api.jar" ]