# Pull base image
From tomcat:8-jre8
# Maintainer
MAINTAINER "Shrikar Jagdale <gsjagdale@gmail.com">
ENV port.http=$PORT
EXPOSE $PORT
# Copy to images tomcat path
ADD target/db-explorer.war /usr/local/tomcat/webapps/
CMD bash -C 'java $JAVA_OPTS -jar target/dependency/webapp-runner.jar --port $PORT target/*.war'
