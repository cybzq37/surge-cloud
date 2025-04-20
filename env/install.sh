cat <<EOF
 1.[install surge-auth]
 2.[install surge-system]
 3.[install surge-gateway]
 6.[install surge-device]
 7.[install surge-map]
 8.[install surge-station]
 9.[install surge-minedata]
 10.[install surge-device-netty]
 11.[install surge-route]
 99.[exit]
EOF

# 切换到当前脚本所在路径，并打印路径
WORKDIR=$(cd $(dirname $0); pwd)

#no.1
read -p "please input the num you want(1-99):" num
expr $num +1 &>/dev/null
if [ "$?" -eq 0 ]; then
  echo "input int"
  exit 1
elif [ "$num" -eq 0 ]; then
  echo "input error"
  exit 1
fi

#surge-auth
[ $num -eq 1 ] && {
  echo "install surge-auth start .........."
  docker rm -f surge-auth
  docker rmi surge-auth:$SC_IMAGE_VERSION
  sleep 3
  cd $WORKDIR/surge-auth
  mkdir -p ./APP
  sh $WORKDIR/surge-auth/dockerfile.sh
  sleep 5
  echo "启动服务......"
  docker run --restart=always --name surge-auth --net=host -v /data/app_data:/APP -d surge-auth:$SC_IMAGE_VERSION
  echo "instal surge-auth finish.........."
}

#surge-system
[ $num -eq 2 ] && {
  echo "install surge-system start .........."
  docker rm -f surge-system
  docker rmi surge-system:$SC_IMAGE_VERSION
  sleep 3
  cd $WORKDIR/surge-system
  mkdir -p ./APP
  sh $WORKDIR/surge-system/dockerfile.sh
  sleep 5
  echo "启动服务......"
  docker run --restart=always --name surge-system --net=host -v /data/app_data:/APP -d surge-system:$SC_IMAGE_VERSION
  echo "instal surge-system finish.........."
}

#surge-gateway
[ $num -eq 3 ] && {
  echo "install surge-gateway start .........."
  docker rm -f surge-gateway
  docker rmi surge-gateway:$SC_IMAGE_VERSION
  sleep 3
  cd $WORKDIR/surge-gateway
  mkdir -p ./APP
  sh $WORKDIR/surge-gateway/dockerfile.sh
  sleep 5
  echo "启动服务......"
  docker run --restart=always --name surge-gateway --net=host -v /data/app_data:/APP -d surge-gateway:$SC_IMAGE_VERSION
  echo "instal surge-gateway finish.........."
}

#surge-device
[ $num -eq 6 ] && {
  echo "install surge-device start .........."
  docker rm -f surge-device
  docker rmi surge-device:$SC_IMAGE_VERSION
  sleep 3
  cd $WORKDIR/surge-device
  mkdir -p ./APP
  sh $WORKDIR/surge-device/dockerfile.sh
  sleep 5
  echo "启动服务......"
  docker run --restart=always --name surge-device --net=host -v /data/app_data:/APP -d surge-device:$SC_IMAGE_VERSION
  echo "instal surge-device finish.........."
}

#surge-map
[ $num -eq 7 ] && {
  echo "install surge-map start .........."
  docker rm -f surge-map
  docker rmi surge-map:$SC_IMAGE_VERSION
  sleep 3
  cd $WORKDIR/surge-map
  mkdir -p ./APP
  sh $WORKDIR/surge-map/dockerfile.sh
  sleep 5
  echo "启动服务......"
  docker run --restart=always --name surge-map --net=host -v /data/app_data:/APP -d surge-map:$SC_IMAGE_VERSION
  echo "instal surge-map finish.........."
}

#surge-station
[ $num -eq 8 ] && {
  echo "install surge-station start .........."
  docker rm -f surge-station
  docker rmi surge-station:$SC_IMAGE_VERSION
  sleep 3
  cd $WORKDIR/surge-station
  mkdir -p ./APP
  sh $WORKDIR/surge-station/dockerfile.sh
  sleep 5
  echo "启动服务......"
  docker run --restart=always --name surge-station --net=host -v /data/app_data:/APP -d surge-station:$SC_IMAGE_VERSION
  echo "instal surge-station finish.........."
}

#surge-minedata
[ $num -eq 9 ] && {
  echo "install surge-minedata start .........."
  docker rm -f surge-minedata
  docker rmi surge-minedata:$SC_IMAGE_VERSION
  sleep 3
  cd $WORKDIR/surge-minedata
  mkdir -p ./APP
  sh $WORKDIR/surge-minedata/dockerfile.sh
  sleep 5
  echo "启动服务......"
  docker run --restart=always --name surge-minedata --net=host -v /data/app_data:/APP -d surge-minedata:$SC_IMAGE_VERSION
  echo "instal surge-minedata finish.........."
}

#surge-deivce-netty
[ $num -eq 10 ] && {
  echo "install surge-device-netty start .........."
  docker rm -f surge-device-netty
  docker rmi surge-device-netty:$SC_IMAGE_VERSION
  sleep 3
  cd $WORKDIR/surge-device-netty
  mkdir -p ./APP
  sh $WORKDIR/surge-device-netty/dockerfile.sh
  sleep 5
  echo "启动服务......"
  docker run --restart=always --name surge-device-netty --net=host -v /data/app_data:/APP -d surge-device-netty:$SC_IMAGE_VERSION
  echo "instal surge-device-netty finish.........."
}

#surge-route
[ $num -eq 11 ] && {
  echo "install surge-route start .........."
  docker rm -f surge-route
  docker rmi surge-route:$SC_IMAGE_VERSION
  sleep 3
  cd $WORKDIR/surge-route
  mkdir -p ./APP
  sh $WORKDIR/surge-route/dockerfile.sh
  sleep 5
  echo "启动服务......"
  docker run --restart=always --name surge-route --net=host -v /data/app_data:/APP -d surge-route:$SC_IMAGE_VERSION
  echo "instal surge-route finish.........."
}

#退出
[ $num -eq 99 ] && {
  exit 1
}
