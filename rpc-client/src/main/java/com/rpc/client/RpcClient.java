package com.rpc.client;

import com.rpc.client.handler.RpcClientHandler;
import com.rpc.common.Request;
import com.rpc.common.Response;
import com.rpc.common.RpcSynFuture;
import com.rpc.common.com.rpc.common.codec.MessageDecoder;
import com.rpc.common.com.rpc.common.codec.MessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by bodhi on 2016/5/13.
 */
public class RpcClient {

    Logger LOG = LoggerFactory.getLogger(RpcClient.class);

    /**
     * client 执行在线程池中,不然会引起outofmemory
     */
    static  Executor executor = Executors.newFixedThreadPool(3);
    private String host;
    private int port;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /* 用于同步响应结果的map, 每次发起一次rpc调用，将用future等待响应结果*/
    private final Map<String, RpcSynFuture<Response>> synResMap = new HashMap<String, RpcSynFuture<Response>>();

    public Response send(Request request) throws InterruptedException, ExecutionException {


        EventLoopGroup workerGroup = new NioEventLoopGroup(0,executor);
        Response response = null;
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new MessageEncoder(Request.class));
                    ch.pipeline().addLast(new MessageDecoder(Response.class));
                    ch.pipeline().addLast(new RpcClientHandler(synResMap));
                }
            });

            b.option(ChannelOption.TCP_NODELAY, true);

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();


            long startMills = System.currentTimeMillis();
            synResMap.put(request.getRequestId(),new RpcSynFuture<Response>());
            ChannelFuture future = f.channel().writeAndFlush(request).sync();
            try {
                response = synResMap.get(request.getRequestId()).get(3000, TimeUnit.MINUTES);

            } catch (TimeoutException e) {
                LOG.warn(String.format("[%s] 调用超时 [%s]", request, 3000));
            }

            long endMills = System.currentTimeMillis();

            LOG.debug(String.format("调用用时: %d 毫秒",(endMills - startMills)));
            if (isClearup && synResMap.size() > 1024) {
                isClearup = false;
                clearSynResMap();
            }
            // Wait until the connection is closed.
            f.channel().close().sync();

        } finally {

            /**
             * 对于同步的请求,需要立即关闭
             */
            workerGroup.shutdownGracefully(0, 0, TimeUnit.MILLISECONDS);
        }

        return response;
    }

    /* 删除过多的keys */
    volatile boolean isClearup = true;


    private void clearSynResMap() {
        LOG.debug("清理rpc调用future，开始...");
        new Thread(new Runnable() {
            public void run() {
                for (String key : synResMap.keySet()) {
                    if (synResMap.get(key).isUsed())
                        synResMap.remove(key);
                }
                isClearup = true;
                LOG.debug("清理rpc调用future，完毕");
            }
        }).start();
    }
}
