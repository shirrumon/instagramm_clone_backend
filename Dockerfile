FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/libs/grafogramm-all.jar /app/grafogramm-all.jar
ENTRYPOINT ["java","-jar","/app/grafogramm-all.jar"]