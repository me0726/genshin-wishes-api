package com.uf.genshinwishes.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author me 2021-06-03 14:53
 */
@Configuration
public class RestConfig {
    @Autowired
    private ProjectProperties properties;

    @Bean
    public RestTemplate restTemplate() {
        var restTemplate = new RestTemplate();
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getClientConnectTimeout());
        factory.setReadTimeout(properties.getClientReadTimeout());
        restTemplate.setRequestFactory(factory);
        return restTemplate;
    }
}
