FROM java:8-jre
MAINTAINER Kunteng cyria <cyria@kunteng.com>

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

ADD ./target/dashboard-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/dashboard-service.jar"]

EXPOSE 10000
