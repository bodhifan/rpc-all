package com.rpc.common.com.rpc.common.codec;

import com.rpc.common.com.rpc.common.utils.SerializtionIOUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by bodhi on 2016/5/13.
 */
public class MessageEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;

    public MessageEncoder(Class<?> genericClazz)
    {
        this.genericClass = genericClazz;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
            throws Exception
    {
        if (genericClass.isInstance(msg))
        {
            byte[] bytes = SerializtionIOUtils.serialize(msg);
            out.writeInt(bytes.length);
            out.writeBytes(bytes);
        }
    }
}
