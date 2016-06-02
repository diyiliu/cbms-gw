package com.tianze.task.impl;

import com.tianze.cache.ICache;
import com.tianze.dao.VehicleInfoDao;
import com.tianze.entity.VehicleInfoEntity;
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
 * Created:Wolf-(2014-12-17 09:50)
 * Version: 1.0
 * Updated:
 */
@Component
public class VehicleInfoTaskImpl implements TaskBase {
    protected Logger logger = LoggerFactory.getLogger(VehicleInfoTaskImpl.class);

    @Resource(name = "vehicleInfoDaoImpl")
    private VehicleInfoDao vehicleInfoDao;
    @Resource(name = "vehicleInfoCacheProvider")
    private ICache cacheProvider ;

    @Override
    public void executeTask() {
        try {
            long start = System.currentTimeMillis();
            logger.info("VehicleInfoTask is starting");
            List res = vehicleInfoDao.selectVehicle();
            this.dealResult(res);
            long end = System.currentTimeMillis();
            logger.info("VehicleInfoTask has been finished,and cost " + (end - start) / 1000 + "s");
        } catch (Exception re) {
            logger.error("VehicleInfoTask error:", re);
        }
    }

    private void dealResult(List<VehicleInfoEntity> res) {
        //cacheProvider.clear();s
        Set keys =  cacheProvider.getKeys();
        Set temp = new HashSet(res.size());
        if (null == res || 0 == res.size()) {
            logger.info("VehicleInfoTask 任务，获取车辆信息为空");
            return;
        }
        for (VehicleInfoEntity entity : res) {
            cacheProvider.put(entity.getVinCode(), entity);
            temp.add(entity.getVinCode());
        }
        Collection<String> subKeys = CollectionUtils.subtract(keys, temp);
        for(String tempKey:subKeys){
            cacheProvider.remove(tempKey);
        }
    }
}
