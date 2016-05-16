package com.rpc.client.handler; /*
 * DESCRIPTION
 *     TODO
 *
 * NOTES
 *    <other useful comments, qualifications, etc.>
 *
 * MODIFIED    (MM/DD/YY)
 *   bofan     2016/5/13 - Creation
 *
 */

import com.rpc.common.Response;
import com.rpc.common.RpcSynFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;

public class RpcClientHandler extends SimpleChannelInboundHandler<Response> {

    private Map<String,RpcSynFuture<Response>> synResMap;
    public RpcClientHandler(Map<String,RpcSynFuture<Response>> futureMap)
    {
        synResMap = futureMap;
    }
    protected void channelRead0(ChannelHandlerContext ctx, Response response) throws Exception {

        String reqId = response.getRequestId();
        if(synResMap.containsKey(reqId))
        {
            synResMap.get(reqId).hasDone(response);
        }
    }
}
