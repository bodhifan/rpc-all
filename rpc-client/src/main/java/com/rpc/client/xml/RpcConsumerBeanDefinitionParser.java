package com.rpc.client.xml;

import com.rpc.client.RpcClientProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bodhi on 6/11/16.
 */
public class RpcConsumerBeanDefinitionParser implements BeanDefinitionParser {

    Logger LOG = LoggerFactory.getLogger(RpcConsumerBeanDefinitionParser.class);

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String interfaceName = element.getAttribute("interface-name");
        String version = element.getAttribute("version");

        LOG.info("构建远程接口存根:" + interfaceName + "\t" + version);

        /**
         * 通过接口和版本获得 client的stub,并将其保存在context中
         */

        try {
            Class interCls = Class.forName(interfaceName);

            Object stubObj = new RpcClientProxy().proxyTarget(interCls,version);

            // 定义bean definition
            RootBeanDefinition beanDefinition = new RootBeanDefinition(StubProxy.class);
            beanDefinition.getPropertyValues().addPropertyValue("intefaceStub",stubObj);
            beanDefinition.getPropertyValues().addPropertyValue("intefaceName",interfaceName);

            String beanName = element.getAttribute("id");
            if (parserContext.getRegistry().containsBeanDefinition(beanName)) {
                throw new Exception(beanName + " 已经注册,请更换id或name");
            }
            else
                parserContext.getRegistry().registerBeanDefinition(beanName,beanDefinition);

            return beanDefinition;
        } catch (ClassNotFoundException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();

        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }

        return  null;
    }
}
