package com.surge.device.gnss.nmea.netty;

import com.surge.common.core.threadpool.GlobalThreadPool;
import com.surge.common.core.utils.SpringUtils;
import com.surge.device.domain.entity.SatelliteInfo;
import com.surge.device.gnss.config.NmeaProperties;
import com.surge.device.gnss.nmea.NmeaParser;
import com.surge.device.gnss.service.SatelliteInfoService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@ChannelHandler.Sharable
public class NmeaNettyChannelHandler extends SimpleChannelInboundHandler<String> {

    private NmeaNettyTcpClient nmeaNettyTcpClient;
    private NmeaProperties nmeaProperties;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) {
        GlobalThreadPool.EXECUTOR.execute(() -> {
            Thread.currentThread().setName("NMEA-parse-thread-" + System.currentTimeMillis());
            if(msg.contains("GSV")) {
                List<SatelliteInfo> satelliteInfoList = NmeaParser.parseGSV(msg);
                for(SatelliteInfo satelliteInfo : satelliteInfoList) {
                    satelliteInfo.setStationEpid(nmeaProperties.getEpid());
                }
                SatelliteInfoService satelliteInfoService = SpringUtils.getBean(SatelliteInfoService.class);
                satelliteInfoService.batchSaveOrUpdate(nmeaProperties, satelliteInfoList);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("NMEA {} {}:{} 协议解析错误：{}", nmeaProperties.getName(), nmeaProperties.getHost(), nmeaProperties.getPort(), cause);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("NMEA {} {}:{} 协议尝试重连", nmeaProperties.getName(), nmeaProperties.getHost(), nmeaProperties.getPort());
        ctx.channel().eventLoop().schedule(() -> nmeaNettyTcpClient.connect(), 60L, TimeUnit.SECONDS); // 尝试重连
        super.channelInactive(ctx);
    }
}
