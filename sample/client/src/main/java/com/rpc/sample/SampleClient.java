package com.rpc.sample;
import com.rpc.services.EchoService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by bodhi on 6/12/16.
 */
public class SampleClient {

    public static void main(String[] args)
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:META-INF/application-context.xml");
        EchoService service = (EchoService) context.getBean("consumer1");

        for (int i = 0;i < 1000;i++) {
            String msg = service.echo(i+"\t hi");
            System.out.println(">>>>>>" + msg);
        }
    }

}
