package com.tianze.task.impl;

import com.tianze.cache.ICache;
import com.tianze.dao.AlarmInfoDao;
import com.tianze.entity.AlarmInfoEntity;
import com.tianze.entity.FenceEntity;
import com.tianze.task.TaskBase;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-10-26 15:10)
 * Version: 1.0
 * Updated:
 */
@Component
public class AlarmInfoTaskImpl implements TaskBase {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource(name = "alarmInfoDaoImpl")
    private AlarmInfoDao dao;
    @Resource(name = "alarmInfoCacheProvider")
    private ICache cacheProvider;
    @Resource(name = "fenceAlarmInfoCacheProvider")
    private ICache fenceAlarmInfoCacheProvider;
    @Resource(name = "workAlarmInfoCacheProvider")
    private ICache workAlarmInfoCacheProvider;

    @Override
    public void executeTask() {
        try {
            long start = System.currentTimeMillis();
            logger.info("AlarmInfoTask is starting");
            List<AlarmInfoEntity> res = dao.selectVehicleAlarm();
            this.dealResult(res);
            long end = System.currentTimeMillis();
            logger.info("AlarmInfoTask has been finished,and cost " + (end - start) / 1000 + "s");
        } catch (Exception re) {
            logger.error("AlarmInfoTask error:", re);
        }
    }

    private void dealResult(List<AlarmInfoEntity> res) {
        Set keys = cacheProvider.getKeys();
        Set fenceKeys = fenceAlarmInfoCacheProvider.getKeys();
        Set workKeys = workAlarmInfoCacheProvider.getKeys();
        Set temp = new HashSet(res.size());
        Set fenceTemp = new HashSet();
        Set workTemp = new HashSet();

        if (null == res || 0 == res.size()) {
            logger.info("AlarmInfoTask 任务，获取信息为空");
            return;
        }
        for (AlarmInfoEntity entity : res) {
            String vehicleId = String.valueOf(entity.getVehicleId());
            cacheProvider.put(vehicleId, entity);
            if (entity.getAlarmCategory() == 2) {
                fenceAlarmInfoCacheProvider.put(vehicleId, entity);
                fenceTemp.add(vehicleId);

            }else if (entity.getAlarmCategory() == 3){
                workAlarmInfoCacheProvider.put(vehicleId + entity.getNameKey(), entity);
                workTemp.add(vehicleId + entity.getNameKey());
            }
            temp.add(vehicleId);
        }
        Collection<String> subKeys = CollectionUtils.subtract(keys, temp);
        for (String tempKey : subKeys) {
            cacheProvider.remove(tempKey);
        }

        Collection<String> subFenceKeys = CollectionUtils.subtract(fenceKeys, fenceTemp);
        for (String tempKey : subFenceKeys) {
            fenceAlarmInfoCacheProvider.remove(tempKey);
        }

        Collection<String> subWorkKeys = CollectionUtils.subtract(workKeys, workTemp);
        for (String tempKey : subWorkKeys) {
            workAlarmInfoCacheProvider.remove(tempKey);
        }
    }
}
