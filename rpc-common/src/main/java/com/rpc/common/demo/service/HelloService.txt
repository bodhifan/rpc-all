package com.rpc.common.demo.service; /*
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

public interface HelloService {

    String getSayHelloStr(String name);
    String getSayHello(String name,int i);
    String getSayHello(int i, String name);
}
