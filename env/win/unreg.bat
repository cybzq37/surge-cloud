@echo off
:: 删除用户级别环境变量
REG delete "HKCU\Environment" /F /V SC_HOST_IP
REG delete "HKCU\Environment" /F /V SC_JVM_XMX
REG delete "HKCU\Environment" /F /V SC_JVM_DMS
REG delete "HKCU\Environment" /F /V SC_IMAGE_VERSION
REG delete "HKCU\Environment" /F /V SC_NACOS_ADDRESS
REG delete "HKCU\Environment" /F /V SC_NACOS_NAMESPACE
REG delete "HKCU\Environment" /F /V SC_NACOS_GROUP
REG delete "HKCU\Environment" /F /V SC_NACOS_USERNAME
REG delete "HKCU\Environment" /F /V SC_NACOS_PASSWORD

echo 用户级别环境变量已成功删除。
pause