FROM openjdk:17
EXPOSE 2025
ADD target/clgportal_api.jar clgportal_api.jar
ENTRYPOINT [ "java","-jar","/clgportal_api.jar" ]