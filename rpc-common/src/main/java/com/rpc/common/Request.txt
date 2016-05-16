package com.rpc.common;

import lombok.Data;

/**
 * Created by bodhi on 2016/5/13.
 */
@Data
public class Request
{
    private String interfaceName;
    private String version;
    private String method;

    /**
     * 方法体参数列表
     */
    private Class<?>[] paramTypes;

    /**
     * 参数值
     */
    private Object[]  paramValues;

    private String requestId;

}