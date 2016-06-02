package com.tianze.listener.impl;

import com.tianze.bean.AlertBean;
import com.tianze.bean.ItemNodes;
import com.tianze.bean.WorkAlarmBean;
import com.tianze.cache.ICache;
import com.tianze.entity.AlarmInfoEntity;
import com.tianze.listener.AbstractResponse;
import com.tianze.utils.CommonUtils;
import com.tianze.utils.DateUtils;
import com.tianze.utils.EnumConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.script.ScriptException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Description: WorkAlarmImpl
 * Author: DIYILIU
 * Update: 2016-02-01 10:07
 */
@Service
public class WorkAlarmImpl extends AbstractResponse {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private long CHECK_TIME = 30 * 1000;

    public static Queue<WorkAlarmBean> DEAL_INFO = new ConcurrentLinkedQueue();

    @Resource
    private ICache alertItemCacheProvider;
    @Resource
    private ICache nameKeyCacheProvider;
    @Resource
    private ICache workAlarmInfoCacheProvider;

    @Override
    public void dealMessage() {

        while (!DEAL_INFO.isEmpty()) {

            WorkAlarmBean workAlarmBean = DEAL_INFO.poll();

            String softVersion = workAlarmBean.getSoftVersion();

            if (!alertItemCacheProvider.containsKey(softVersion) || !nameKeyCacheProvider.containsKey(softVersion)) {
                continue;
            }

            Map<String, AlertBean> alertItem = (Map<String, AlertBean>) alertItemCacheProvider.get(softVersion);
            Map<String, ItemNodes> nameKeys = (Map<String, ItemNodes>) nameKeyCacheProvider.get(softVersion);

            Set<Map.Entry<String, AlertBean>> alertSet = alertItem.entrySet();
            try {
                for (Iterator<Map.Entry<String, AlertBean>> iterator = alertSet.iterator(); iterator.hasNext(); ) {
                    Map.Entry<String, AlertBean> entry = iterator.next();
                    String alertKey = entry.getKey();
                    AlertBean alertBean = entry.getValue();
                    if (setValue(nameKeys, alertBean, workAlarmBean.getItemValue())) {
                        dealAlarm(workAlarmBean, alertKey, alertBean.getDescription(), alertBean.doAlert());
                    }
                }
            } catch (ScriptException e) {
                logger.error("处理工况报警错误！{}", e);
            }
        }
    }

    private boolean setValue(Map<String, ItemNodes> nameKeys, AlertBean alertBean, Map values) {

        Map params = alertBean.getParams();
        for (Iterator<Map.Entry> iterator = params.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry entry = iterator.next();
            String key = (String) entry.getKey();
            if (nameKeys.containsKey(key)) {
                String field = nameKeys.get(key).getField();
                Object value = values.get(field.toUpperCase());
                if (value == null) {
                    return false;
                }
                params.put(key, value);
            }
        }
        alertBean.setParams(params);

        return true;
    }

    private void dealAlarm(final WorkAlarmBean alarmBean, final String alertKey, final String description, boolean isAlarm) {

        if (workAlarmInfoCacheProvider.containsKey(alarmBean.getVehicleId() + alertKey)) {

            // 取消报警
            if (!isAlarm) {
                //update 操作
                StringBuffer strb = new StringBuffer("update ")
                        .append(EnumConfig.DbConfig.DB_USER_CBMS)
                        .append(".")
                        .append(EnumConfig.DbConfig.VEHICLE_ALARM)
                        .append(" set ENDTIME=").append("to_date('")
                        .append(DateUtils.formatDate(alarmBean.getGpsTime(), DateUtils.All_DAY_FORMAT))
                        .append("' , 'yyyy-mm-dd hh24-mi-ss')")
                        .append(",ENDLNG=")
                        .append(alarmBean.getLng())
                        .append(",ENDLAT=")
                        .append(alarmBean.getLat())
                        .append(",STATUS=2")
                        .append(" where ENDTIME is null")
                        .append(" and VEHICLEID=").append(alarmBean.getVehicleId())
                        .append(" and ALARMKEY='").append(alertKey).append("'");

                CommonUtils.dealInfoTODb(strb.toString());
                workAlarmInfoCacheProvider.remove(alarmBean.getVehicleId() + alertKey);
            }
        } else {
            // 开始报警
            if (isAlarm) {
                Map insertMap = new ConcurrentHashMap();
                insertMap.put("VEHICLEID", alarmBean.getVehicleId());
                insertMap.put("ALARMCATEGORY", 3);
                insertMap.put("STARTLNG", alarmBean.getLng());
                insertMap.put("STARTTIME", alarmBean.getGpsTime());
                insertMap.put("STARTLAT", alarmBean.getLat());
                insertMap.put("DETAIL", description);
                insertMap.put("ALARMKEY", alertKey);
                insertMap.put("STATUS", 1);

                CommonUtils.dealInfoTODb(EnumConfig.DbConfig.VEHICLE_ALARM, EnumConfig.DbConfig.DB_USER_CBMS, insertMap);
                workAlarmInfoCacheProvider.put(alarmBean.getVehicleId() + alertKey, new AlarmInfoEntity(alarmBean.getVehicleId(), 3,
                        alarmBean.getGpsTime(), alarmBean.getLng(), alarmBean.getLat(), alertKey));
            }
        }
    }

    @Override
    public long getCheckTime() {
        return this.CHECK_TIME;
    }
}
