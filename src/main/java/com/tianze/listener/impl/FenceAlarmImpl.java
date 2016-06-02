package com.tianze.listener.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tianze.bean.*;
import com.tianze.cache.ICache;
import com.tianze.entity.AlarmInfoEntity;
import com.tianze.entity.FenceEntity;
import com.tianze.listener.AbstractResponse;
import com.tianze.utils.CommonUtils;
import com.tianze.utils.DateUtils;
import com.tianze.utils.EnumConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Description: 处理围栏报警
 * Author: Wolf
 * Created:Wolf-(2015-10-23 17:10)
 * Version: 1.0
 * Updated:
 */
@Service
public class FenceAlarmImpl extends AbstractResponse {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private long CHECK_TIME = 30 * 1000;
    public static Queue<FenceAlarmBean> DEAL_INFO = new ConcurrentLinkedQueue();
    @Resource(name = "fenceAlarmInfoCacheProvider")
    private ICache fenceAlarmInfoCacheProvider;
    @Resource(name = "fenceCacheProvider")
    private ICache fenceCacheProvider;

    @Resource(name = "inOutAlarmInfoCacheProvider")
    private ICache inOutAlarmInfoCacheProvider;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public long getCheckTime() {
        return this.CHECK_TIME;
    }

    @Override
    public void dealMessage() {
        final Date now = new Date();
        while (!DEAL_INFO.isEmpty()) {
            final FenceAlarmBean bean = DEAL_INFO.poll();
            final int vehicleId = bean.getVehicleId();
            String vehicleIdStr = String.valueOf(vehicleId);
            //判断报警策略里面有没有该车的报警
            if (!fenceCacheProvider.containsKey(vehicleIdStr)) {
                continue;
            }
            FenceEntity fenceEntity = (FenceEntity) fenceCacheProvider.get(vehicleIdStr);

            boolean isAlarm = fenceAlarmInfoCacheProvider.containsKey(String.valueOf(vehicleId));

            int alarmType = fenceEntity.getAlarmType();
            try {
                switch (alarmType) {
                    case 1:
                        this.outAlarm(fenceEntity, bean, isAlarm);
                        ;
                        break;
                    case 2:
                        this.inAlarm(fenceEntity, bean, isAlarm);
                        ;
                        break;
                    case 3:
                        this.allAlarm(fenceEntity, bean);
                        ;
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                logger.error("json to object error:", e);
                //e.printStackTrace();
            }

        }
    }

    /**
     * @param fenceEntity    围栏策略
     * @param fenceAlarmBean 车辆当前 经纬度
     * @param isAlarm        是否已经报警
     */
    private void outAlarm(final FenceEntity fenceEntity, final FenceAlarmBean fenceAlarmBean, boolean isAlarm) throws IOException {
        final int vehicleId = fenceAlarmBean.getVehicleId();
        String vehicleIdStr = String.valueOf(vehicleId);
        //判断车辆是进还是出围栏
        String fence = fenceEntity.getGeoInfo();
        int sharp = fenceEntity.getSharp();
        int isIn = isInOrOut(sharp, fence, fenceAlarmBean);
        if (isAlarm) {
            //已经报警
            if (0 != isIn) {//在内，结束报警
                //update 操作
                StringBuffer sb = new StringBuffer("update ")
                        .append(EnumConfig.DbConfig.DB_USER_CBMS)
                        .append(".")
                        .append(EnumConfig.DbConfig.VEHICLE_ALARM)
                        .append(" set ENDTIME=").append("to_date('")
                        .append(DateUtils.formatDate(fenceAlarmBean.getGpsTime(), DateUtils.All_DAY_FORMAT))
                        .append("' , 'yyyy-mm-dd hh24-mi-ss')")
                        .append(",ENDLNG=")
                        .append(fenceAlarmBean.getLng())
                        .append(",ENDLAT=")
                        .append(fenceAlarmBean.getLat())
                        .append(",STATUS=2")
                        //.append(" where VEHICLEID=")
                        //.append(vehicleId)
                        .append(" where ENDTIME is null");
                CommonUtils.dealInfoTODb(sb.toString());
                //删除缓存
                fenceAlarmInfoCacheProvider.remove(vehicleIdStr);
            }
        } else {
            if (0 == isIn) {//新增报警
                final Date insertTime = fenceAlarmBean.getGpsTime();
                CommonUtils.dealInfoTODb(EnumConfig.DbConfig.VEHICLE_ALARM, EnumConfig.DbConfig.DB_USER_CBMS, new ConcurrentHashMap() {{
                    this.put("VEHICLEID", vehicleId);
                    this.put("ALARMCATEGORY", 2);
                    this.put("STARTTIME", insertTime);
                    this.put("STARTLNG", fenceAlarmBean.getLng());
                    this.put("STARTLAT", fenceAlarmBean.getLat());
                    this.put("FENCEID", fenceEntity.getId());
                    this.put("STATUS", 1);
                }});

                //添加到缓存
                fenceAlarmInfoCacheProvider.put(vehicleIdStr, new AlarmInfoEntity(vehicleId, 2, insertTime, fenceAlarmBean.getLng(), fenceAlarmBean.getLat(), fenceEntity.getId()));
            }
        }
    }

    private void inAlarm(final FenceEntity fenceEntity, final FenceAlarmBean fenceAlarmBean, boolean isAlarm) throws IOException {
        final int vehicleId = fenceAlarmBean.getVehicleId();
        String vehicleIdStr = String.valueOf(vehicleId);
        //判断车辆是进还是出围栏
        String fence = fenceEntity.getGeoInfo();
        int sharp = fenceEntity.getSharp();
        int isIn = isInOrOut(sharp, fence, fenceAlarmBean);
        if (isAlarm) {
            //已经报警
            if (0 == isIn) {//在外，结束报警
                //update 操作
                StringBuffer sb = new StringBuffer("update ")
                        .append(EnumConfig.DbConfig.DB_USER_CBMS)
                        .append(".")
                        .append(EnumConfig.DbConfig.VEHICLE_ALARM)
                        .append(" set ENDTIME=").append("to_date('")
                        .append(DateUtils.formatDate(fenceAlarmBean.getGpsTime(), DateUtils.All_DAY_FORMAT))
                        .append("' , 'yyyy-mm-dd hh24-mi-ss')")
                        .append(",ENDLNG=")
                        .append(fenceAlarmBean.getLng())
                        .append(",ENDLAT=")
                        .append(fenceAlarmBean.getLat())
                        .append(",STATUS=2")
                        //.append(" where VEHICLEID=")
                        //.append(vehicleId)
                        .append(" where ENDTIME is null");
                CommonUtils.dealInfoTODb(sb.toString());
                //删除缓存
                fenceAlarmInfoCacheProvider.remove(vehicleIdStr);
            }
        } else {
            if (0 != isIn) {//新增报警
                final Date insertTime = fenceAlarmBean.getGpsTime();
                CommonUtils.dealInfoTODb(EnumConfig.DbConfig.VEHICLE_ALARM, EnumConfig.DbConfig.DB_USER_CBMS, new ConcurrentHashMap() {{
                    this.put("VEHICLEID", vehicleId);
                    this.put("ALARMCATEGORY", 2);
                    this.put("STARTTIME", insertTime);
                    this.put("STARTLNG", fenceAlarmBean.getLng());
                    this.put("STARTLAT", fenceAlarmBean.getLat());
                    this.put("FENCEID", fenceEntity.getId());
                    this.put("STATUS", 1);
                }});

                //添加到缓存
                fenceAlarmInfoCacheProvider.put(vehicleIdStr, new AlarmInfoEntity(vehicleId, 2, insertTime, fenceAlarmBean.getLng(), fenceAlarmBean.getLat(), fenceEntity.getId()));
            }
        }
    }

    private void allAlarm(FenceEntity fenceEntity, final FenceAlarmBean fenceAlarmBean) throws IOException {
        final int vehicleId = fenceAlarmBean.getVehicleId();
        String vehicleIdStr = String.valueOf(vehicleId);
        InOrOutBean temp = (InOrOutBean) inOutAlarmInfoCacheProvider.get(vehicleIdStr);
        String fence = fenceEntity.getGeoInfo();
        int sharp = fenceEntity.getSharp();
        int isIn = isInOrOut(sharp, fence, fenceAlarmBean);
        if (null == temp) {
            temp = new InOrOutBean(fenceAlarmBean.getLng(), fenceAlarmBean.getLat(), fenceAlarmBean.getGpsTime(), isIn);
            inOutAlarmInfoCacheProvider.put(vehicleIdStr, temp);
            //fenceAlarmInfoCacheProvider.put(vehicleIdStr, new AlarmInfoEntity(vehicleId, 2, fenceAlarmBean.getGpsTime(), fenceAlarmBean.getLng(), fenceAlarmBean.getLat(), fenceEntity.getId()));
            return;
        }
        //如果存在缓存
        int tempIsIn = temp.getInOrOut();
        if ((isIn == 0 && tempIsIn != 0) || (isIn != 0 && tempIsIn == 0)) {//报警结束
            Map insertMap = new ConcurrentHashMap();
            insertMap.put("VEHICLEID", vehicleId);
            insertMap.put("ALARMCATEGORY", 2);
            insertMap.put("STARTTIME", temp.getTime());
            insertMap.put("STARTLNG", temp.getLng());
            insertMap.put("STARTLAT", temp.getLat());
            insertMap.put("ENDTIME", fenceAlarmBean.getGpsTime());
            insertMap.put("ENDLNG", fenceAlarmBean.getLng());
            insertMap.put("ENDLAT", fenceAlarmBean.getLat());
            insertMap.put("FENCEID", fenceEntity.getId());
            insertMap.put("STATUS", 2);
            CommonUtils.dealInfoTODb(EnumConfig.DbConfig.VEHICLE_ALARM, EnumConfig.DbConfig.DB_USER_CBMS, insertMap);
            inOutAlarmInfoCacheProvider.put(vehicleIdStr, new InOrOutBean(fenceAlarmBean.getLng(), fenceAlarmBean.getLat(), fenceAlarmBean.getGpsTime(), isIn));
            //fenceAlarmInfoCacheProvider.put(vehicleIdStr, new AlarmInfoEntity(vehicleId, 2, fenceAlarmBean.getGpsTime(), fenceAlarmBean.getLng(), fenceAlarmBean.getLat(), fenceEntity.getId()));
        }

    }

    private int isInOrOut(int sharp, String fence, FenceAlarmBean fenceAlarmBean) throws IOException {
        // 0 在外
        int isIn = -1;
        //1 圆形 2 多边形
        //判断车辆是进还是出围栏
        if (1 == sharp) {
            Map fenceMap = objectMapper.readValue(fence, Map.class);
            Circle circle = new Circle(new Point(Double.parseDouble(fenceMap.get("Lng").toString()), Double.parseDouble(fenceMap.get("Lat").toString())), (int) Double.parseDouble(fenceMap.get("Radius").toString()));
            isIn = circle.isPointInArea(new Point(fenceAlarmBean.getLng(), fenceAlarmBean.getLat()));

        } else if (2 == sharp) {
            Point[] points = objectMapper.readValue(fence, Point[].class);
            Polygon polygon = new Polygon(points);
            isIn = polygon.isPointInArea(new Point(fenceAlarmBean.getLng(), fenceAlarmBean.getLat()));
        }

        return isIn;
    }
}
