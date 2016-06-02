package com.tianze.listener;

import com.tianze.utils.SpringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2014-04-21 10:59)
 * Version: 1.0
 * Updated:
 */
@Service
public class ListenerStart {
    private ExecutorService pool = null;
    private Collection<ListernInter> listernInterManagers = null;
    public void start() {
        ApplicationContext applicationContext = SpringUtils.getApplicationContext();
        Map<String, ListernInter> nameBeanMap = applicationContext.getBeansOfType(ListernInter.class);
        listernInterManagers = nameBeanMap.values();
        if (!CollectionUtils.isEmpty(listernInterManagers)) {
            pool = Executors.newFixedThreadPool(listernInterManagers.size());
            for (ListernInter listern : listernInterManagers) {
                pool.submit(listern);
            }
        }
    }
}
