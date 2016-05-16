package com.rpc.common.com.rpc.common.codec;

import com.rpc.common.com.rpc.common.utils.SerializtionIOUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by bodhi on 2016/5/13.
 */
public class MessageDecoder extends ByteToMessageDecoder
{
    Logger LOG = LoggerFactory.getLogger(this.getClass());
    private Class<?> genericClass;

    public MessageDecoder(Class<?> genericCls)
    {
        this.genericClass = genericCls;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
                          List<Object> out) throws Exception
    {
        // 读取四个字节的长度
        if (in.readableBytes() < 4)
        {
            LOG.debug("长度小于4,返回");
            return;
        }

        in.markReaderIndex();
        int len = in.readInt();

        // 如果当前字节小于 len 则返回继续等待
        if(in.readableBytes() < len)
        {
            LOG.debug(String.format("没有足够长的字节%d，等待下次读取",len));
            in.resetReaderIndex();
            return;
        }

        byte[] message = new byte[len];
        in.readBytes(message);

        Object request = SerializtionIOUtils.deserialize(message, genericClass);
        out.add(request);
    }

}