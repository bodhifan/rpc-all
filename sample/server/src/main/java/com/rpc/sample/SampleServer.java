package com.rpc.sample;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by bodhi on 6/12/16.
 */
public class SampleServer {


    public static void main(String[] args) throws Exception {
        new ClassPathXmlApplicationContext("spring-dao.xml");
    }
}
