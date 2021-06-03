package com.uf.genshinwishes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Properties;

@ConfigurationPropertiesScan
@SpringBootApplication
@EnableScheduling
public class GenshinWishesApplication {


    public static void main(String[] args) {
        Properties prop = System.getProperties();
        prop.setProperty("http.proxyHost", "127.0.0.1");
        prop.setProperty("http.proxyPort", "10808");
        prop.setProperty("https.proxyHost", "127.0.0.1");
        prop.setProperty("https.proxyPort", "10808");
        SpringApplication.run(GenshinWishesApplication.class, args);
    }

}


