package com.rpc.xml.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by bodhi on 6/11/16.
 */
public class MyTest {

    public static void main(String[] args)
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:META-INF/application-context.xml");

    }
}
