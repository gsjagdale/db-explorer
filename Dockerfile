# Pull base image
From tomcat:8-jre8
# Maintainer
MAINTAINER "Shrikar Jagdale <gsjagdale@gmail.com">
ENV port.http=$PORT
# Copy to images tomcat path
ADD target/db-explorer.war /usr/local/tomcat/webapps/
