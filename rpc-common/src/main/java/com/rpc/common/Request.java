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
     * ����������б�
     */
    private Class<?>[] paramTypes;

    /**
     * ����ֵ
     */
    private Object[]  paramValues;

    private String requestId;

}