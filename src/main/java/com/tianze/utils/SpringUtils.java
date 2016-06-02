package com.tianze.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-08-28 17:00)
 * Version: 1.0
 * Updated:
 */
public class SpringUtils {
    private static ApplicationContext applicationContext;
    private static Logger LOGGER = LoggerFactory.getLogger(SpringUtils.class);

    /**
     * 获取Spring的上下文
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        if (null == applicationContext) {
            LOGGER.info("create applicationContext!");
            applicationContext = new ClassPathXmlApplicationContext("spring-*.xml");
            /*applicationContext = new FileSystemXmlApplicationContext(new String[]{"/config/spring-context.xml","/config/spring-task.xml"});*/
        }
        return applicationContext;
    }


}
