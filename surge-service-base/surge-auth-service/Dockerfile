FROM openjdk:8-jdk

LABEL maintainer="lichunqing11612@surgenfo.com"

RUN rm -f /etc/localtime \
&& ln -sv /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& echo "Asia/Shanghai" > /etc/timezone

RUN mkdir -p /APP/logs \
    /APP/tmp \
    /APP/skywalking/agent

ENV LANG C.UTF-8
ENV LC_ALL C.UTF-8

WORKDIR /APP

ARG SC_APP_NAME
ENV SC_APP_NAME ${SC_APP_NAME}

ARG SC_HOST_IP
ENV SC_HOST_IP ${SC_HOST_IP}

ARG SC_JVM_XMX
ENV SC_JVM_XMX ${SC_JVM_XMX}

ARG SC_JVM_DMS
ENV SC_JVM_DMS ${SC_JVM_DMS}

ARG SC_NACOS_ADDRESS
ENV SC_NACOS_ADDRESS ${SC_NACOS_ADDRESS}

ARG SC_NACOS_USERNAME
ENV SC_NACOS_USERNAME ${SC_NACOS_USERNAME}

ARG SC_NACOS_PASSWORD
ENV SC_NACOS_PASSWORD ${SC_NACOS_PASSWORD}

ARG SC_NACOS_NAMESPACE
ENV SC_NACOS_NAMESPACE ${SC_NACOS_NAMESPACE}

ARG SC_NACOS_GROUP
ENV SC_NACOS_GROUP ${SC_NACOS_GROUP}

COPY surge-*.jar /${SC_APP_NAME}.jar

ENTRYPOINT ["bash", "-c", "source ~/.bashrc && java -jar  \
            -Xms${SC_JVM_XMX} \
            -Xmx${SC_JVM_XMX} \
            -Dfile.encoding=\"UTF-8\" \
            -Dsun.jnu.encoding=\"UTF-8\" \
            -XX:MaxDirectMemorySize=${SC_JVM_DMS} \
            -Djava.awt.headless=true \
            -Djava.security.egd=file:/dev/./urandom \
            -XX:+HeapDumpOnOutOfMemoryError \
            -Djava.io.tmpdir=/APP/tmp \
            -DDUBBO_IP_TO_REGISTRY=${SC_HOST_IP} \
            # -Dskywalking.agent.service_name=${SC_APP_NAME} \
            # -javaagent:/surge/skywalking/agent/skywalking-agent.jar \
            /${SC_APP_NAME}.jar"]