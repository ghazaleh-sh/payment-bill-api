#FROM tomcat:latest
#USER root
#ARG DEBIAN_FRONTEND=noninteractive
#RUN apt-get update \
#    && apt-get -yq  install apt-utils \
#    && apt-get -yq  install curl \
#    && apt-get -yq  install telnet \
#    && apt-get -yq  install net-tools \
#    && apt-get install -y ca-certificates-java
#
#COPY target/payment-bill-api-0.0.1-SNAPSHOT.jar /usr/local/tomcat/webapps/
#RUN sed -i 's/port="8080"/port="9080"/' /usr/local/tomcat/conf/server.xml
#
#RUN echo "Env variable :  $JAVA_OPTS"
#ENV TZ=Asia/Tehran
#RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#ENV JPDA_TRANSPORT="dt_socket"
#ENV JPDA_ADDRESS="*:8000"
#
##ENV JAVA_OPTS="-Dspring.config.location=/opt/configs/ebp/"
#ENV CATALINA_OPTS="-Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=1616 -Dcom.sun.management.jmxremote.rmi.port=1616 -Dcom.sun.management.jmxremote.local.only=false"
#
#EXPOSE 9080 8000 1616
#CMD ["catalina.sh" , "jpda", "run"]

# mvn clean package -P test -Dskiptests=true
# clean package -P test -DskipTests=true
FROM adoptopenjdk/openjdk11

VOLUME /tmp

ENV TZ=Asia/Tehran

RUN  mkdir -p /var/log/payment-bill-api-0
RUN  chmod -R 777 /var/log/payment-bill-api-0

COPY target/*.jar payment-bill-api-0.0.1-SNAPSHOT.jar
#ENTRYPOINT ["java","-Xdebug","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8517","-jar","/payment-bill-api-0.0.1-SNAPSHOT.jar"]