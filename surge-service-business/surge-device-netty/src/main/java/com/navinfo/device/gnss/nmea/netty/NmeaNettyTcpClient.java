package com.surge.device.gnss.nmea.netty;

import com.surge.device.gnss.config.NmeaProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class NmeaNettyTcpClient {

    private NmeaProperties nmeaProperties;
    private Bootstrap bootstrap;
    private ChannelFuture channelFuture;

    public NmeaNettyTcpClient(NmeaProperties nmeaProperties) {
        this.nmeaProperties = nmeaProperties;
    }

    public Bootstrap init(){
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 15*1000);
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() { //通道处理者
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast("idleStateHandler", new IdleStateHandler(15,0,0, TimeUnit.MINUTES))
                                .addLast(new LineBasedFrameDecoder(2048))
                                .addLast(new StringDecoder())
                                .addLast("clientChannelHandler", new NmeaNettyChannelHandler(NmeaNettyTcpClient.this, nmeaProperties));
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE,true);
        return bootstrap;
    }

    public void connect(){
        channelFuture = bootstrap.connect(nmeaProperties.getHost(), nmeaProperties.getPort());
        channelFuture.addListener((ChannelFutureListener) f -> {
            if (f != null && f.isSuccess()) {
                log.info("{} NMEA tcp服务 {}:{} 连接成功", nmeaProperties.getName(),
                        nmeaProperties.getHost(), nmeaProperties.getPort());
            }else {
                Throwable cause = f.cause();
                log.error("{} NMEA tcp服务 {}:{} 连接失败，错误原因：{}", nmeaProperties.getName(),
                        nmeaProperties.getHost(), nmeaProperties.getPort(), cause);
                f.channel().eventLoop().schedule(() -> this.connect(), 30L, TimeUnit.SECONDS);
            }
        });
    }
}
