package com.rpc.server.handler;

import com.rpc.common.Request;
import com.rpc.common.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by bodhi on 2016/5/13.
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<Request> {

    Logger LOG = LoggerFactory.getLogger(RpcServerHandler.class);

    private Map<String,Object> serviceMaps;

    public RpcServerHandler(Map<String,Object> services)
    {
        serviceMaps = services;
    }


    /* 处理远程调用请求 */
    public Object invokeMethod(Request request) throws Exception {

        String interfaceName = getInterfaceKey(request);
        if (!serviceMaps.containsKey(interfaceName)) {

            String msg = String.format("don't exist interface [%s] at version [%s]",request.getInterfaceName(),request.getVersion());
            LOG.debug(msg);
            throw new Exception(msg);
        }

        boolean isInvokeSuc = false;
        Object rtn = null;
        Object instance = serviceMaps.get(interfaceName);
        Class<?>[] interfaces = instance.getClass().getInterfaces();

        /* 遍历所有的接口,查找目标接口*/
        for (Class<?> interfaze: interfaces) {

            if (interfaze.getCanonicalName().equals(request.getInterfaceName()))
            {
                Method[] methods = interfaze.getMethods();
                /* 遍历接口中的方法，查找目标方法 */
                for (Method m: methods ) {
                    if (m.getName().equals(request.getMethod()))
                    {
                        Class<?>[] paramTypes = m.getParameterTypes();
                        if (paramTypes.length != request.getParamTypes().length)
                            continue;

                        int cnt = 0;
                        for (Class<?> paramType: paramTypes) {
                            if (!paramType.getCanonicalName().equals(request.getParamTypes()[cnt++].getCanonicalName()))
                                break;

                        }

                        /* 只有参数长度和参数类型完全一致的情况下才进行调用*/
                        if (cnt == paramTypes.length) {
                            rtn = m.invoke(instance, request.getParamValues());
                            isInvokeSuc = true;
                            break;
                        }
                    }
                }

                // 如果已经调用成功，退出查询
                if (isInvokeSuc)
                    break;
            }
        }

        if (!isInvokeSuc)
        {
            String msg = String.format("don't exist methods [%s] with [%d] params ",request.getMethod(),request.getParamValues().length);
            LOG.debug(msg);
            throw new Exception(msg);
        }

        return  rtn;
    }

    private String getInterfaceKey(Request request) {
        return String.format("%s-%s",request.getInterfaceName(),request.getVersion());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Request request) throws Exception {
        LOG.debug(String.format("receive call request: %s",request.toString()));
        Response response = new Response();
        response.setRequestId(request.getRequestId());
        try
        {
            // 假装超时
//            int mills = new Random().nextInt(3);
//            if (mills%2==0)
//                Thread.sleep(mills*1000);
            response.setValue(invokeMethod(request));
            response.setSuccess(true);
        }
        catch (Exception e)
        {
            response.setSuccess(false);
            response.setExceptionMsg(e.getMessage());
            response.setValue(null);
        }

        LOG.debug(String.format("response: %s",response.toString()));

        /* 返回结果给客户端 */
        channelHandlerContext.writeAndFlush(response).sync();
    }
}