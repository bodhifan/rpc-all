package com.rpc.server.impl; /*
 * DESCRIPTION
 *     TODO
 *
 * NOTES
 *    <other useful comments, qualifications, etc.>
 *
 * MODIFIED    (MM/DD/YY)
 *   bofan     2016/5/13 - Creation
 *
 */

import com.rpc.common.demo.service.HelloService;
import com.rpc.server.services.RpcService;

@RpcService(service = "com.rpc.common.demo.service.HelloService")
public class HelloServiceImpl implements HelloService{

    public String getSayHelloStr(String name) {
        return String.format("hi [ %s ] this is server",name);
    }

    public String getSayHello(String name, int i) {
        return String.format("this is string and int");
    }

    public String getSayHello(int i, String name) {
        return String.format("this is int and string");
    }
}
