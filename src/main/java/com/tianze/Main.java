package com.tianze;

import com.tianze.client.DbpClient;
import com.tianze.factory.AbstractProFactory;
import com.tianze.listener.ListenerStart;
import com.tianze.protocol.AbstractBaseParser;
import com.tianze.server.IServer;
import com.tianze.server.TcpServer;
import com.tianze.utils.SpringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.Map;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-08-28 16:59)
 * Version: 1.0
 * Updated:
 */
public class Main {
    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        //加载spring
        applicationContext = SpringUtils.getApplicationContext();
        startListener();
        startFactory();
        //IServer tcpServer = new TcpServer();
        //tcpServer.init();
    }

    public static void startListener() {
        ListenerStart listener = (ListenerStart) applicationContext.getBean("listenerStart");
        listener.start();
    }



    public static void startFactory() {
        Map<String, AbstractProFactory> factoryMap = applicationContext.getBeansOfType(AbstractProFactory.class);
        //ICache cache = (ICache) applicationContext.getBean("protocolCache");
        Collection<AbstractProFactory> factorys = factoryMap.values();
        if (!CollectionUtils.isEmpty(factorys)) {
            for (AbstractProFactory factory : factorys) {
                factory.init();
            }
        }
    }

    public static void testSomething() {
        DbpClient.queue.add("select * from a");
        DbpClient.queue.add("select * from b");
        DbpClient.queue.add("select * from c");
    }
}
