package com.yes.http.container;

import com.yes.http.server.ApiServer;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.plugins.server.resourcefactory.SingletonResource;
import org.jboss.resteasy.spi.metadata.ResourceClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Lubin.Xuan on 2015/1/27.
 * ie.
 */
public class AcfApiContainer extends AcfContainer {
    private static final Logger logger = LoggerFactory.getLogger(AcfApiContainer.class);

    @Autowired
    private ApiServer apiServer;

    @Override
    public void deployResource() {
        try {
            deployment.getRegistry().addResourceFactory(new SingletonResource(apiServer, new ResourceClass(ApiServer.class, "")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer port() {
        String acfPort = "8080";
        if (StringUtils.isBlank(acfPort)) {
            return null;
        }
        return Integer.parseInt(acfPort);
    }

}
