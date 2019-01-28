# Pull base image
#From tomcat:8-jre8
FROM java:8
VOLUME /tmp
# Maintainer
#MAINTAINER "Shrikar Jagdale <gsjagdale@gmail.com">

#ENV PORT=80
#EXPOSE 80
# Copy to images tomcat path
ADD target/dependency/webapp-runner.jar /tmp/webapp-runner.jar
ADD target/db-explorer.war /tmp/db-explorer.war
#CMD bash -C 'java $JAVA_OPTS -jar /tmp/webapp-runner.jar --port $PORT /tmp/*.war'
#CMD bash -C 'java -version'
#CMD ["/usr/bin/java", "-jar", "/tmp/webapp-runner.jar", "--port", "$PORT", "/tmp/*.war"]
CMD /usr/bin/java -jar /tmp/webapp-runner.jar --port $PORT /tmp/*.war
