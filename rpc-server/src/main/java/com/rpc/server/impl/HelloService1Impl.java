package com.rpc.server.impl; /*
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

import com.rpc.common.demo.service.HelloService;
import com.rpc.server.services.RpcService;

@RpcService(service = "com.rpc.common.demo.service.HelloService",version = "1.0.1")
public class HelloService1Impl implements HelloService{
    public String getSayHelloStr(String name) {
        return null;
    }

    public String getSayHello(String name, int i) {
        return null;
    }

    public String getSayHello(int i, String name) {
        return "this is 1.0.1 version";
    }
}
