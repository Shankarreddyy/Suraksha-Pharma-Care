package com.suaksha.pharma.care.pharmacy.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

@Configuration
public class RequiredBeansCreation {

    @Primary
    @Bean
    public RestClient.Builder restClientBuilder(){
        return RestClient.builder();
    }
    @Bean
    public RestClient loadbalencerrestClient(@Qualifier("loadbalencedrestClientBuilder") RestClient.Builder restClientBuilder){
        return restClientBuilder.build();
    }
    @LoadBalanced
    @Bean
    public RestClient.Builder loadbalencedrestClientBuilder(){
        return RestClient.builder();
    }

}
