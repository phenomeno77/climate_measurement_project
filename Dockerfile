FROM maven:3-jdk-11
RUN adduser --disabled-password nonroot
USER nonroot
RUN mkdir /home/nonroot/app
WORKDIR /home/nonroot/app
COPY src  ./src
COPY pom.xml ./
ENTRYPOINT ["mvn"]
CMD ["spring-boot:run"]
