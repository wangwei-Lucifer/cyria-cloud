FROM java:8-jre
MAINTAINER Kunteng cyria <cyria@kunteng.com>

ADD ./target/dashboard-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/dashboard-service.jar"]

EXPOSE 10000
