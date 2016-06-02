package com.tianze.task.impl;

import com.tianze.cache.ICache;
import com.tianze.dao.AlarmInfoDao;
import com.tianze.dao.FenceDao;
import com.tianze.entity.FenceEntity;
import com.tianze.entity.VehicleInfoEntity;
import com.tianze.task.AbstractTask;
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
 * Created:Wolf-(2015-10-26 15:07)
 * Version: 1.0
 * Updated:
 */
@Component
public class FenceTaskImpl implements TaskBase {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource(name = "fenceDaoImpl")
    private FenceDao dao;
    @Resource(name = "fenceCacheProvider")
    private ICache cacheProvider;

    @Override
    public void executeTask() {
        try {
            long start = System.currentTimeMillis();
            logger.info("FenceTaskImpl is starting");
            List<FenceEntity> res = dao.getAll();
            this.dealResult(res);
            long end = System.currentTimeMillis();
            logger.info("FenceTaskImpl has been finished,and cost " + (end - start) / 1000 + "s");
        } catch (Exception re) {
            logger.error("FenceTaskImpl error:", re);
        }
    }

    private void dealResult(List<FenceEntity> res) {
//cacheProvider.clear();
        Set keys = cacheProvider.getKeys();
        Set temp = new HashSet(res.size());
        if (null == res || 0 == res.size()) {
            logger.info("FenceTaskImpl 任务，获取信息为空");
            return;
        }
        for (FenceEntity entity : res) {
            cacheProvider.put(String.valueOf(entity.getVehicleId()), entity);
            temp.add(String.valueOf(entity.getVehicleId()));
        }
        Collection<String> subKeys = CollectionUtils.subtract(keys, temp);
        for (String tempKey : subKeys) {
            cacheProvider.remove(tempKey);
        }
    }
}
