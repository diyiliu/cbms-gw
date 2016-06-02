package com.tianze.task.impl;

import com.tianze.cache.ICache;
import com.tianze.dao.CanInfoDao;
import com.tianze.entity.CanInfoEntity;
import com.tianze.task.TaskBase;
import com.tianze.utils.CanUtils;
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
 * Description: 功能集读取
 * Author: Wolf
 * Created:Wolf-(2014-12-17 13:56)
 * Version: 1.0
 * Updated:
 */
@Component
public class CanTaskImpl implements TaskBase {
    protected Logger logger = LoggerFactory.getLogger(VehicleInfoTaskImpl.class);

    @Resource(name = "canInfoDaoImpl")
    private CanInfoDao dao;
    @Resource(name = "canInfoCacheProvider")
    private ICache cacheProvider;
    @Resource
    private CanUtils canUtils;

    @Resource(name = "canPackageProvider")
    private ICache canPackageProvider;

    @Resource(name = "canStatusProvider")
    private ICache canStatusProvider;

    @Resource(name = "paramsCacheProvider")
    private ICache paramsCacheProvider;

    @Resource(name = "nameKeyCacheProvider")
    private ICache nameKeyCacheProvider;

    @Resource(name = "alertItemCacheProvider")
    private ICache alertItemCacheProvider;

    @Override
    public void executeTask() {
        try {
            long start = System.currentTimeMillis();
            logger.info("CanTaskImpl is starting");
            List res = dao.selectVehicle();
            this.dealResult(res);
            long end = System.currentTimeMillis();
            logger.info("CanTaskImpl has been finished,and cost " + (end - start) / 1000 + "s");
        } catch (Exception re) {
            logger.error("CanTaskImpl error:", re);
        }
    }

    private void dealResult(List<CanInfoEntity> res) {
        //cacheProvider.clear();
        //canPackageProvider.clear();
        //canStatusProvider.clear();
        Set keys =  cacheProvider.getKeys();
        Set temp = new HashSet(res.size());

        Set packageKeys =  canPackageProvider.getKeys();
        Set packageTemp = new HashSet();

        Set statusKeys =  canStatusProvider.getKeys();
        Set statusTemp = new HashSet();


        Set paramsKeys =  paramsCacheProvider.getKeys();
        Set paramsTemp = new HashSet();

        Set nameKeyKeys =  nameKeyCacheProvider.getKeys();
        Set nameKeyTemp = new HashSet();

        Set alertItemKeys =  alertItemCacheProvider.getKeys();
        Set alertItemTemp = new HashSet();

        if (null == res || 0 == res.size()) {
            logger.info("CanTaskImpl 任务，获取功能集为空");
            return;
        }
        for (CanInfoEntity entity : res) {
            cacheProvider.put(entity.getSoftVersionCode(), entity);
            temp.add(entity.getSoftVersionCode());
            statusTemp.add(entity.getSoftVersionCode());
            packageTemp.add(entity.getSoftVersionCode());
            paramsTemp.add(entity.getSoftVersionCode());
            nameKeyTemp.add(entity.getSoftVersionCode());
            alertItemTemp.add(entity.getSoftVersionCode());
            //解析CAN
            canUtils.dealCan(entity);
        }

        Collection<String> subKeys = CollectionUtils.subtract(keys, temp);
        for(String tempKey: subKeys){
            cacheProvider.remove(tempKey);
        }

        Collection<String> subPackageKeys = CollectionUtils.subtract(packageKeys, packageTemp);
        for(String tempKey: subPackageKeys){
            canPackageProvider.remove(tempKey);
        }
        Collection<String> subStatusKeys = CollectionUtils.subtract(statusKeys, statusTemp);
        for(String tempKey: subStatusKeys){
            canStatusProvider.remove(tempKey);
        }

        Collection<String> subParamsKeys = CollectionUtils.subtract(paramsKeys, paramsTemp);
        for(String tempKey: subParamsKeys){
            paramsCacheProvider.remove(tempKey);
        }

        Collection<String> subNameKeyKeys = CollectionUtils.subtract(nameKeyKeys, nameKeyTemp);
        for(String tempKey: subNameKeyKeys){
            nameKeyCacheProvider.remove(tempKey);
        }

        Collection<String> subAlertItemKeys = CollectionUtils.subtract(alertItemKeys, alertItemTemp);
        for(String tempKey: subAlertItemKeys){
            alertItemCacheProvider.remove(tempKey);
        }
    }
}