package com.rpc.client; /*
 * DESCRIPTION
 *     TODO
 *
 * NOTES
 *    <other useful comments, qualifications, etc.>
 *
 * MODIFIED    (MM/DD/YY)
 *   bofan     2016/5/16 - Creation
 *
 */

import com.rpc.common.Request;
import com.rpc.common.Response;
import com.rpc.common.demo.service.HelloService;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcClientProxy implements MethodInterceptor {

    Logger LOG = LoggerFactory.getLogger(RpcClientProxy.class);

    private Class<?> interfaze;
    static Map<String,String> versionMaps = new ConcurrentHashMap<String, String>();
    @SuppressWarnings("unchecked")
    public Object proxyTarget(Class<?> clazz,String version) {

        versionMaps.put(clazz.getCanonicalName(),version);
        this.interfaze = clazz;
        Enhancer en = new Enhancer();
        en.setInterfaces(new Class[]{clazz});
        en.setCallback(new RpcClientProxy());
        Object tt = en.create();
        return tt;
    }
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        Request req = new Request();
        req.setRequestId(System.currentTimeMillis()+"");
        req.setInterfaceName(o.getClass().getInterfaces()[0].getCanonicalName());
        req.setVersion(versionMaps.get(o.getClass().getInterfaces()[0].getCanonicalName()));
        req.setMethod(method.getName());
        req.setParamTypes(method.getParameterTypes());
        req.setParamValues(objects);


        RpcClient client = new RpcClient("127.0.0.1",8080);
        Response response = client.send(req);

        return  response.getValue();
    }
}
