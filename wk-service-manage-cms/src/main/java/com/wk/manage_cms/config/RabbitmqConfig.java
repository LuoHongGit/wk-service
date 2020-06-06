package com.wk.manage_cms.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * rabbitmq配置类
 */
@Configurable
public class RabbitmqConfig {
    //交换机的名称
    public static final String  EX_ROUTING_CMS_POSTPAGE = "ex_routing_cms_postpage";

    /**
     * 配置交换机
     * @return
     */
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange EXCHANGE_TOPICS_INFORM() {
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }
}
