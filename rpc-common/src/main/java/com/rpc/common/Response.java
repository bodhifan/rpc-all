package com.rpc.common;

import lombok.Data;
/**
 * Created by bodhi on 2016/5/13.
 */
@Data
public class Response
{

    /** ���ز���ֵ */
    Object value;
    boolean isSuccess;

    String requestId;
    /**
     * �쳣��Ϣ
     */
    String exceptionMsg;
}
