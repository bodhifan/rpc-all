package com.rpc.services;

import com.rpc.server.services.RpcService;

/**
 * Created by bodhi on 6/12/16.
 */

@RpcService(service = "com.rpc.services.EchoService",version = "1.0.0")
public class EchoServiceImp implements EchoService {
    public String echo(String msg) {
        return "echo " + msg;
    }
}
