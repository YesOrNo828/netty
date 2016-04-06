package com.yes;

import com.adtime.common.util.PropHelper;
import com.yes.common.LogBackInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.File;

/**
 * Created by 叶贤勋 on 2015/9/22.
 */
public class Start {
    public static final Logger logger = LoggerFactory.getLogger(Start.class);

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        initLogBack();
        try {

            logger.info("Start system initialize spring context.......");
            ClassPathXmlApplicationContext apxac = new ClassPathXmlApplicationContext("classpath*:META-INF/spring/*.xml");
            logger.info("System started initialize spring context success...........total time cost {} S", (System.currentTimeMillis() - start) / 1000);
        } catch (Exception e) {
            logger.error("System start failed due to {}", e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Linux 无法正确找到LogBack 配置文件 需要指定 tomcat 环境不需要指定
     */
    private static void initLogBack() {
        if ("/".equals(File.separator)) {
            Resource resource = new DefaultResourceLoader().getResource("classpath:" + PropHelper.get("logback", "logback.xml"));
            try {
                File externalConfigFile = resource.getFile();
                System.out.println(externalConfigFile);
                LogBackInit.init(externalConfigFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
