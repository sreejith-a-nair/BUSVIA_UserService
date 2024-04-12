package com.busvia.userservice.config;

import com.busvia.userservice.client.AuthClient;
import com.busvia.userservice.client.BusRootClient;
import feign.Feign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.service.invoker.HttpServiceProxyFactoryExtensionsKt;

@Configuration
public class WebClientConfig {

    @Autowired
    private LoadBalancedExchangeFilterFunction filterFunction;

    @Bean
    public WebClient driverWebClient(){
        return WebClient.builder()
                .baseUrl("http://auth-service")
                .filter(filterFunction)
                .build();
    }

    @Bean
    public AuthClient userClient(){

        HttpServiceProxyFactory httpServiceProxyFactory
                = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(driverWebClient()))
                .build();
        return  httpServiceProxyFactory.createClient(AuthClient.class);

    }


    @Bean
    public WebClient busRootWebClient(){
        return WebClient.builder()
                .baseUrl("http://bus-service")
                .filter(filterFunction)
                .build();
    }

    @Bean
    public BusRootClient busRootClient(){

        HttpServiceProxyFactory httpServiceProxyFactory
                = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(busRootWebClient()))
                .build();
        return  httpServiceProxyFactory.createClient(BusRootClient.class);

    }
}
