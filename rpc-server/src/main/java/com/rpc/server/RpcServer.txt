package com.rpc.server;

import com.rpc.common.Request;
import com.rpc.common.Response;
import com.rpc.common.com.rpc.common.codec.MessageDecoder;
import com.rpc.common.com.rpc.common.codec.MessageEncoder;
import com.rpc.server.com.rpc.server.handler.RpcServerHandler;
import com.rpc.server.services.RpcService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bodhi on 2016/5/13.
 */
public class RpcServer implements InitializingBean,ApplicationContextAware {


    Logger LOG = LoggerFactory.getLogger(RpcServer.class);

    Map<String,Object> serviceMaps = new HashMap<String, Object>();

    ApplicationContext appCtx;

    @Setter
    private int port;
    public RpcServer()
    {
    }
    public RpcServer(int port)
    {
        this.port = port;

    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new MessageDecoder(Request.class));
                            ch.pipeline().addLast(new MessageEncoder(Response.class));
                            ch.pipeline().addLast(new RpcServerHandler(serviceMaps));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /* 加载服务类 */
    public void loadServiceClazz()
    {
        Map<String,Object> serviceBeans = appCtx.getBeansWithAnnotation(RpcService.class);
        for(Map.Entry<String,Object> pair : serviceBeans.entrySet())
        {
            LOG.debug(">>>>>>>>>>>>>加载服务："  + pair.getValue());

            // 获取 serviceAnnotation的值
            RpcService serviceAnnotation = pair.getValue().getClass().getAnnotation(RpcService.class);
            String intefaceName = serviceAnnotation.service();
            String version = serviceAnnotation.version();

            String serviceKey = generateServiceKey(intefaceName,version);
            serviceMaps.put(serviceKey,pair.getValue());
        }
    }

    private String generateServiceKey(String intefaceName, String version) {

        return intefaceName+"-"+version;
    }

    public static void main(String[] args) throws Exception {
        new ClassPathXmlApplicationContext("spring-dao.xml");
    }

    /* 加载service类和启动netty服务器*/
    public void afterPropertiesSet() throws Exception {

        loadServiceClazz();
        run();
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.appCtx = applicationContext;
    }
}
