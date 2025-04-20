#!/bin/bash
SC_APP_NAME="surge-device"
SC_IMAGE_VERSION="1.0.0"

docker build \
--build-arg SC_JVM_XMX=${SC_JVM_XMX} \
--build-arg SC_JVM_DMS=${SC_JVM_DMS} \
--build-arg SC_NACOS_ADDRESS=${SC_NACOS_ADDRESS} \
--build-arg SC_NACOS_USERNAME=${SC_NACOS_USERNAME} \
--build-arg SC_NACOS_PASSWORD=${SC_NACOS_PASSWORD} \
--build-arg SC_NACOS_NAMESPACE=${SC_NACOS_NAMESPACE} \
--build-arg SC_NACOS_GROUP=${SC_NACOS_GROUP} \
--build-arg SC_HOST_IP=${SC_HOST_IP} \
--build-arg SC_APP_NAME=${SC_APP_NAME} \
-t ${SC_APP_NAME}:${SC_IMAGE_VERSION} \
.