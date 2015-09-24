package com.yes.http.server;

import com.adtime.common.web.ControllerHelper;
import com.adtime.common.web.ResponseVO;
import com.yes.entity.User;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("{app}/{service}/{method}")
@Produces(MediaType.APPLICATION_JSON + "; " + MediaType.CHARSET_PARAMETER + "=UTF-8")
public class ApiServer {

    private static final Logger logger = LoggerFactory.getLogger(ApiServer.class);

    @GET
    public ResponseVO get(@PathParam("app") String app,
                          @PathParam("service") String service,
                          @PathParam("method") String method,
                          @MatrixParam("v") String version,
                          @Context HttpRequest request,
                          @Context HttpResponse response) throws Exception {

        return invokeRequest(app, service, method, version, request, response);
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_FORM_URLENCODED})/*MediaType.TEXT_XML,MediaType.APPLICATION_XML,*/
    public ResponseVO post(@PathParam("app") String app,
                           @PathParam("service") String service,
                           @PathParam("method") String method,
                           @MatrixParam("v") String version,
                           @Context HttpRequest request,
                           @Context HttpResponse response) throws Exception {

        return invokeRequest(app, service, method, version, request, response);
    }

    @PUT
    public ResponseVO put(@PathParam("app") String app,
                          @PathParam("service") String service,
                          @PathParam("method") String method,
                          @MatrixParam("v") String version,
                          @Context HttpRequest request,
                          @Context HttpResponse response) throws Exception {

        return invokeRequest(app, service, method, version, request, response);
    }

    @DELETE
    public ResponseVO delete(@PathParam("app") String app,
                             @PathParam("service") String service,
                             @PathParam("method") String method,
                             @MatrixParam("v") String version,
                             @Context HttpRequest request,
                             @Context HttpResponse response) throws Exception {

        return invokeRequest(app, service, method, version, request, response);
    }

    private ResponseVO invokeRequest(String app, String service, String method, String version, HttpRequest request, HttpResponse response) throws Exception {
        logger.info("app:{},service:{},version:{},request:{},response:{}", app, service, version, request, response);
        return ControllerHelper.success(new User(1, method));
    }
}
