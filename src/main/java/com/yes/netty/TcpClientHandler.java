package com.yes.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by叶贤勋 on 2015/8/5.
 */
public class TcpClientHandler  extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(TcpClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        //messageReceived方法,名称很别扭，像是一个内部方法.
        logger.info("client接收到服务器返回的消息:"+msg);

    }

}