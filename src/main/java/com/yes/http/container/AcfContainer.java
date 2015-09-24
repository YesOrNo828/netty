package com.yes.http.container;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yes.http.provider.FastJsonConfig;
import com.yes.http.provider.FastJsonProvider;
import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lubin.Xuan on 2015/2/10.
 * ie.
 */
public abstract class AcfContainer {

    private static final Logger logger = LoggerFactory.getLogger(AcfContainer.class);

    private final NettyJaxrsServer server = new NettyJaxrsServer();

    protected ResteasyDeployment deployment;

    private Integer port = 8080;

    private int executorMultiple;

    private float ioThread;

    private List<Class> deployResourceList;

    public AcfContainer() {
        try {
            port = port();
        } catch (Exception e) {
            port = null;
        }
    }

    private Map<String, String> mediaTypeMappings = new HashMap<>();

    public void setMediaTypeMappings(Map<String, String> mediaTypeMappings) {
        this.mediaTypeMappings = mediaTypeMappings;
    }

    protected void startServer() {
        Thread thread = new Thread(() -> {
            deployment = server.getDeployment();
            if (mediaTypeMappings.isEmpty()) {
                mediaTypeMappings.put("json", "application/json");
            }
            mediaTypeMappings.forEach((key, type) -> deployment.getMediaTypeMappings().put(key, type));

            final SerializerFeature[] serializerFeatures = new SerializerFeature[]{
                    SerializerFeature.DisableCircularReferenceDetect,
                    SerializerFeature.WriteNullListAsEmpty
            };

            FastJsonConfig fastJsonConfig = new FastJsonConfig(new SerializeConfig(), serializerFeatures, null, new ParserConfig(), null);
            FastJsonProvider fastJsonProvider = new FastJsonProvider().init(fastJsonConfig);
            deployment.getProviders().add(fastJsonProvider);

            server.setPort(port);
            server.setIoWorkerCount((int) (8 * ioThread));
            server.setExecutorThreadCount(8 * (executorMultiple < 1 ? 1 : executorMultiple));
            server.setMaxRequestSize(Integer.MAX_VALUE);

            server.start();

            deployResource();
            if (null != deployResourceList && !deployResourceList.isEmpty()) {
                deployResourceList.forEach(r -> deployment.getRegistry().addResourceFactory(new POJOResourceFactory(r)));
            }

            logger.info("服务启动成功 端口:{}", port);

        });
        thread.setName(this.getClass().getSimpleName());
        thread.start();
    }

    public abstract void deployResource();

    public abstract Integer port();
}
