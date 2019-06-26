FROM java:8-jre
MAINTAINER Kunteng cyria <cyria@kunteng.com>

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

VOLUME /app/static
ADD ./target/dashboard-service.jar /app/
ADD ./target/classes/scripts/pack.sh /app/static/scripts/
ADD ./target/classes/static/public/dist /app/static/public/dist/

CMD ["java", "-Xmx200m", "-jar", "/app/dashboard-service.jar"]

EXPOSE 10000
