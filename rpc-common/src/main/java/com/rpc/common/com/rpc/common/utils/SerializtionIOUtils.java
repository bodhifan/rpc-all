package com.rpc.common.com.rpc.common.utils;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * Created by fandonglin on 2016/5/13.
 */
public class SerializtionIOUtils {
    public static <T> byte[] serialize(T message) {

        Class<T> clazz= (Class<T>) message.getClass();
        Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(clazz);
        LinkedBuffer buffer = LinkedBuffer.allocate(4096);
        byte[] protostuff = null;
        try {
            protostuff = ProtostuffIOUtil.toByteArray(message, schema, buffer);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            buffer.clear();
        }
        return protostuff;
    }

    public static <T> T deserialize(byte[] bytesList,Class<T> clazz) {

        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T message = null;
        try
        {
            message = clazz.newInstance();
        } catch (InstantiationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ProtostuffIOUtil.mergeFrom(bytesList,message,schema);

        return message;

    }
}
