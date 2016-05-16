package com.rpc.common;

import lombok.Data;
/**
 * Created by bodhi on 2016/5/13.
 */
@Data
public class Response
{

    /** 返回参数 */
    Object value;
    boolean isSuccess;

    String requestId;
    /**
     * 异常信息
     */
    String exceptionMsg;
}
