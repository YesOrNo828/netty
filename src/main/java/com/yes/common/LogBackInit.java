package com.yes.common;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Linux 无法正确找到LogBack 配置文件 需要指定 tomcat 环境不需要指定
 */
public class LogBackInit {
    public static void init(File externalConfigFile) {
        try {
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            if (!externalConfigFile.exists()) {
                throw new IOException("Logback External Config File Parameter does not reference a file that exists");
            } else {
                if (!externalConfigFile.isFile()) {
                    throw new IOException("Logback External Config File Parameter exists, but does not reference a file");
                } else {
                    if (!externalConfigFile.canRead()) {
                        throw new IOException("Logback External Config File exists and is a file, but cannot be read.");
                    } else {
                        JoranConfigurator configurator = new JoranConfigurator();
                        configurator.setContext(lc);
                        lc.reset();
                        configurator.doConfigure(externalConfigFile);
                        StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
