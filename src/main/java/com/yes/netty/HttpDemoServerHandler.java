package com.yes.netty;
import com.alibaba.fastjson.JSONObject;
import com.yes.entity.User;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;

public class HttpDemoServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private static final Logger logger = Logger.getLogger(HttpDemoServerHandler.class.getName());

    private DefaultFullHttpRequest fullHttpRequest;

    private boolean readingChunks;

    private final StringBuilder responseContent = new StringBuilder();

    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); //Disk

    private HttpPostRequestDecoder decoder;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (decoder != null) {
            decoder.cleanFiles();
        }
    }

    public void messageReceived(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        fullHttpRequest = (DefaultFullHttpRequest) msg;

        if (HttpServer.isSSL) {
            System.out.println("Your session is protected by " +
                    ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite() +
                    " cipher suite.\n");
        }
        /**
         * �ڷ������˴�ӡ������Ϣ
         */
        System.out.println("VERSION: " + fullHttpRequest.getProtocolVersion().text() + "\r\n");
        System.out.println("REQUEST_URI: " + fullHttpRequest.getUri() + "\r\n\r\n");
        System.out.println("\r\n\r\n");
        for (Entry<String, String> entry : fullHttpRequest.headers()) {
            System.out.println("HEADER: " + entry.getKey() + '=' + entry.getValue() + "\r\n");
        }

        /**
         * �������˷�����Ϣ
         */
        responseContent.setLength(0);
        responseContent.append("WELCOME TO THE WILD WILD WEB SERVER\r\n");
        responseContent.append("===================================\r\n");

        responseContent.append("VERSION: " + fullHttpRequest.getProtocolVersion().text() + "\r\n");
        responseContent.append("REQUEST_URI: " + fullHttpRequest.getUri() + "\r\n\r\n");
        responseContent.append("\r\n\r\n");
        for (Entry<String, String> entry : fullHttpRequest.headers()) {
            responseContent.append("HEADER: " + entry.getKey() + '=' + entry.getValue() + "\r\n");
        }
        responseContent.append("\r\n\r\n");
        Set<Cookie> cookies;
        String value = fullHttpRequest.headers().get(COOKIE);
        if (value == null) {
            cookies = Collections.emptySet();
        } else {
            cookies = CookieDecoder.decode(value);
        }
        for (Cookie cookie : cookies) {
            responseContent.append("COOKIE: " + cookie.toString() + "\r\n");
        }
        responseContent.append("\r\n\r\n");

        if (fullHttpRequest.getMethod().equals(HttpMethod.GET)) {
            //get����
            QueryStringDecoder decoderQuery = new QueryStringDecoder(fullHttpRequest.getUri());
            Map<String, List<String>> uriAttributes = decoderQuery.parameters();
            for (Entry<String, List<String>> attr : uriAttributes.entrySet()) {
                for (String attrVal : attr.getValue()) {
                    responseContent.append("URI: " + attr.getKey() + '=' + attrVal + "\r\n");
                }
            }
            responseContent.append("\r\n\r\n");

            responseContent.append("\r\n\r\nEND OF GET CONTENT\r\n");
            writeResponse(ctx.channel());
            return;
        } else if (fullHttpRequest.getMethod().equals(HttpMethod.POST)) {
            //post����
            decoder = new HttpPostRequestDecoder(factory, fullHttpRequest);
            readingChunks = HttpHeaders.isTransferEncodingChunked(fullHttpRequest);
            responseContent.append("Is Chunked: " + readingChunks + "\r\n");
            responseContent.append("IsMultipart: " + decoder.isMultipart() + "\r\n");

            try {
                while (decoder.hasNext()) {
                    InterfaceHttpData data = decoder.next();
                    if (data != null) {
                        try {
                            writeHttpData(data);
                        } finally {
                            data.release();
                        }
                    }
                }
            } catch (EndOfDataDecoderException e1) {
                responseContent.append("\r\n\r\nEND OF POST CONTENT\r\n\r\n");
            }
            writeResponse(ctx.channel());
            return;
        } else {
            System.out.println("discard.......");
            return;
        }
    }

    private void reset() {
        fullHttpRequest = null;
        // destroy the decoder to release all resources
        decoder.destroy();
        decoder = null;
    }

    private void writeHttpData(InterfaceHttpData data) {

        /**
         * HttpDataType����������
         * Attribute, FileUpload, InternalAttribute
         */
        if (data.getHttpDataType() == HttpDataType.Attribute) {
            Attribute attribute = (Attribute) data;
            String value;
            try {
                value = attribute.getValue();
            } catch (IOException e1) {
                e1.printStackTrace();
                responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ":"
                        + attribute.getName() + " Error while reading value: " + e1.getMessage() + "\r\n");
                return;
            }
            if (value.length() > 100) {
                responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ":"
                        + attribute.getName() + " data too long\r\n");
            } else {
                responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ":"
                        + attribute.toString() + "\r\n");
            }
        }
    }

    private AtomicInteger atomicInteger = new AtomicInteger();
    /**
     * http������Ӧ����
     *
     * @param channel
     */
    private void writeResponse(Channel channel) {
        // Convert the response content to a ChannelBuffer.
//        ByteBuf buf = copiedBuffer(responseContent.toString(), CharsetUtil.UTF_8);

        ByteBuf buf = copiedBuffer(JSONObject.toJSONString(new User(atomicInteger.incrementAndGet(), "����")), CharsetUtil.UTF_8);
        responseContent.setLength(0);

        // Decide whether to close the connection or not.
        boolean close = fullHttpRequest.headers().contains(HttpHeaders.Values.CLOSE)
                || fullHttpRequest.getProtocolVersion().equals(HttpVersion.HTTP_1_0)
                && !fullHttpRequest.headers().contains(HttpHeaders.Values.KEEP_ALIVE);

        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(CONTENT_TYPE, "application/json; charset=UTF-8");

        if (!close) {
            // There's no need to add 'Content-Length' header
            // if this is the last response.
            response.headers().set(CONTENT_LENGTH, buf.readableBytes());
        }

        Set<Cookie> cookies;
        String value = fullHttpRequest.headers().get(COOKIE);
        if (value == null) {
            cookies = Collections.emptySet();
        } else {
            cookies = CookieDecoder.decode(value);
        }
        if (!cookies.isEmpty()) {
            // Reset the cookies if necessary.
            for (Cookie cookie : cookies) {
                response.headers().add(SET_COOKIE, ServerCookieEncoder.encode(cookie));
            }
        }
        // Write the response.
        ChannelFuture future = channel.writeAndFlush(response);
        // Close the connection after the write operation is done if necessary.
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.log(Level.WARNING, responseContent.toString(), cause);
        ctx.channel().close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        messageReceived(ctx, msg);
    }
}
