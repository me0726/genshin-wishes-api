package com.uf.genshinwishes.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author me 2021-06-03 10:29
 */
@Data
@ConfigurationProperties("app.genshin")
public class ProjectProperties {

    private Map<String, Map<String, String>> regionSettings;

    private int clientReadTimeout;

    private int clientConnectTimeout;

    private String clientUrl;

}
