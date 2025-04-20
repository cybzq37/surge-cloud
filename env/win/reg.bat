@echo off
:: 注册用户级别环境变量
setx SC_HOST_IP "192.168.1.3"
setx SC_JVM_XMX "1g"
setx SC_JVM_DMS "1g"
setx SC_IMAGE_VERSION "1.0.0"
setx SC_NACOS_ADDRESS "171.17.171.17:8848"
setx SC_NACOS_NAMESPACE "surge-cloud"
setx SC_NACOS_GROUP "DEFAULT_GROUP"
setx SC_NACOS_USERNAME "nacos"
setx SC_NACOS_PASSWORD "nacos"

echo 用户级别环境变量已成功添加。
pause
