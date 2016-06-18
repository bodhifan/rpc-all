package com.rpc.client.xml;

import org.springframework.beans.factory.FactoryBean;

/**
 * Created by bodhi on 6/11/16.
 */
public class StubProxy implements FactoryBean {

    public Object getIntefaceStub() {
        return intefaceStub;
    }

    public void setIntefaceStub(Object intefaceStub) {
        this.intefaceStub = intefaceStub;
    }

    public String getIntefaceName() {
        return intefaceName;
    }

    public void setIntefaceName(String intefaceName) {
        this.intefaceName = intefaceName;
    }

    private String intefaceName;
    /**
     * rpc中的接口存根对象
     */
    Object intefaceStub;

    public Object getObject() throws Exception {
        return intefaceStub;
    }

    public Class<?> getObjectType() {
        return intefaceStub.getClass();
    }

    public boolean isSingleton() {
        return false;
    }
}
