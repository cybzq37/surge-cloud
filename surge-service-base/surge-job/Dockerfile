FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Lion Li

RUN mkdir -p /surge/job/logs \
    /surge/job/temp \
    /surge/skywalking/agent

WORKDIR /surge/job

ENV SERVER_PORT=9203

EXPOSE ${SERVER_PORT}

ADD ./target/surge-job.jar ./app.jar

ENTRYPOINT ["java", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dserver.port=${SERVER_PORT}", \
#            "-Dskywalking.agent.service_name=surge-job", \
#            "-javaagent:/surge/skywalking/agent/skywalking-agent.jar", \
            "-jar", "app.jar"]
