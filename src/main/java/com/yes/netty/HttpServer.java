package com.yes.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/**
 * Created by 叶贤勋 on 2015/8/5.
 */
public class HttpServer {
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private static final String IP = "127.0.0.1";
    private static int PORT = 9999;
    /**用于分配处理业务线程的线程组个数*/
    protected static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors()*2; //默认
    /** 业务出现线程大小*/
    protected static final int BIZTHREADSIZE = 4;
    /*
     * NioEventLoopGroup实际上就是个线程池,
     * NioEventLoopGroup在后台启动了n个NioEventLoop来处理Channel事件,
     * 每一个NioEventLoop负责处理m个Channel,
     * NioEventLoopGroup从NioEventLoop数组里挨个取出NioEventLoop来处理Channel
 */
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);

    public static boolean isSSL;

    public HttpServer(int port) {
        this.PORT = port;
    }

    protected static void run() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                if (HttpServer.isSSL) {
//                    SSLEngine engine = SecureSslContextFactory;
//                    engine.setNeedClientAuth(true); //ssl双向认证
//                    engine.setUseClientMode(false);
//                    engine.setWantClientAuth(true);
//                    engine.setEnabledProtocols(new String[]{"SSLv3"});
//                    pipeline.addLast("ssl", new SslHandler(engine));
                }
                /**
                 * http-request解码器
                 * http服务器端对request解码
                 */
                pipeline.addLast("decoder", new HttpRequestDecoder());
                /**
                 * http-response解码器
                 * http服务器端对response编码
                 */
                pipeline.addLast("encoder", new HttpResponseEncoder());
                pipeline.addLast("aggregator", new HttpObjectAggregator(1048576));
                /**
                 * 压缩
                 * Compresses an HttpMessage and an HttpContent in gzip or deflate encoding
                 * while respecting the "Accept-Encoding" header.
                 * If there is no matching encoding, no compression is done.
                 */
                pipeline.addLast("deflater", new HttpContentCompressor());
                pipeline.addLast("handler", new HttpDemoServerHandler());
            }
        });

        b.bind(IP, PORT).sync();
        logger.info("http服务器已启动");
    }

    protected static void shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        logger.info("开始启动http服务器...");
        int port = 8888;
        isSSL = false;
        new HttpServer(port).run();
    }
}
