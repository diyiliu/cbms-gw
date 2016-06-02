package com.tianze.protocol.cmd;

import com.tianze.bean.FenceAlarmBean;
import com.tianze.bean.GpsInfoBean;
import com.tianze.cache.ICache;
import com.tianze.entity.VehicleInfoEntity;
import com.tianze.handel.ServerHandel;
import com.tianze.listener.impl.FenceAlarmImpl;
import com.tianze.protocol.CbBaseParser;
import com.tianze.thrift.MapLocation;
import com.tianze.utils.CanUtils;
import com.tianze.utils.CommonUtils;
import com.tianze.utils.EnumConfig;
import com.tianze.utils.ThriftUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-09 10:50)
 * Version: 1.0
 * Updated:
 */
@Component
public class Cmd_03 extends CbBaseParser {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final int cmdId = 0x03;
    @Resource
    private CanUtils canUtils;
    @Resource(name = "vehicleInfoCacheProvider")
    private ICache vehicleCache;

    @Resource
    private ICache monitorCacheProvider;

    @Override
    public void parser(Object message, Object ctx) {
        GpsInfoBean bean = (GpsInfoBean) message;
        ChannelHandlerContext channelHandlerContext = (ChannelHandlerContext) ctx;
        ByteBuf bf = channelHandlerContext.alloc().buffer(bean.getMessages().length);
        bf.writeBytes(bean.getMessages());
        bf.markReaderIndex();
        //车辆VIN码长度
        int vinLen = bf.readByte();
        //车辆VIN码
        byte[] vinBytes = new byte[vinLen];
        bf.readBytes(vinBytes);
        String vinCode = new String(vinBytes);
        final VehicleInfoEntity entity = (VehicleInfoEntity) vehicleCache.get(vinCode);
        if (null == entity) {
            LOGGER.debug("不存在该编号的车辆,vincode:{}", vinCode);
            return;
        }
        if (monitorCacheProvider.containsKey(vinCode)) {
            LOGGER.warn("命令：3, 车辆[{}]， 位置数据[{}]]",
                    vinCode, CommonUtils.byteToString(bean.getMessages()));
        }

        //软件版本号长度
        int softVersionLen = bf.readByte();
        //软件版本号
        byte[] softVersionBytes = new byte[softVersionLen];
        bf.readBytes(softVersionBytes);
        final String softVersion = new String(softVersionBytes);
        //经纬度
        final double lat = bf.readInt() / EnumConfig.CommonConfig.LNG_LAT_DIVIDE;
        final double lng = bf.readInt() / EnumConfig.CommonConfig.LNG_LAT_DIVIDE;
        final int speed = bf.readUnsignedByte();
        final int direction = bf.readUnsignedByte();
        final int altitude = bf.readShort();
        int statusLen = bf.readByte();
        byte[] statusBytes = new byte[statusLen];
        bf.readBytes(statusBytes);
        byte[] timeByte = new byte[6];
        bf.readBytes(timeByte);
        final Date gpsTime = CommonUtils.correctionTime(CommonUtils.getGpsTime(timeByte), new Date(), 1);
        Map statusValues = canUtils.parseStatus(statusBytes, softVersion);
        //获取省市区
        final MapLocation area = ThriftUtils.getArea(lat, lng);

        Map updateMap = new ConcurrentHashMap() {{
            this.put("ENCRYPTLAT", lat);
            this.put("ENCRYPTLNG", lng);
            this.put("PROVINCE", area.getProvince());
            this.put("CITY", area.getCity());
            this.put("AREA", area.getTown());
            this.put("SPEED", speed);
            this.put("DIRECTION", direction);
            this.put("ALTITUDE", altitude);
            this.put("GPSTIME", gpsTime);
            this.put("SOFTVERSION", softVersion);
            this.put("SYSTEMTIME", new Date());
        }};
        updateMap.putAll(statusValues);
        CommonUtils.dealInfoTODb(EnumConfig.DbConfig.VEHICLE_CURR, EnumConfig.DbConfig.DB_USER_CBMS, updateMap, new ConcurrentHashMap() {{
            this.put("VEHICLEID", entity.getVehicleId());
        }});

        ServerHandel.VEHICLES.put(vinCode, 1);
        //分析围栏报警
        FenceAlarmImpl.DEAL_INFO.offer(new FenceAlarmBean(entity.getVehicleId(), lat, lng, gpsTime));
    }

    @Override
    public Integer getCmdId() {
        return this.cmdId;
    }
}
