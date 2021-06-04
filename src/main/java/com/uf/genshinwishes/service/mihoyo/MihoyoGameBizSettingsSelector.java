package com.uf.genshinwishes.service.mihoyo;

import com.uf.genshinwishes.config.ProjectProperties;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * @author me 2021-05-31 10:58
 */
@Component
public class MihoyoGameBizSettingsSelector {

    @Autowired
    private ProjectProperties properties;


    public Optional<String> getImEndpoint(String gameBiz) {
        return Optional.of(properties)
            .map(ProjectProperties::getRegionSettings)
            .map(e -> e.get(gameBiz))
            .map(e -> e.get("im-endpoint"));
    }

    public Optional<String> getWishEndpoint(String gameBiz) {
        return Optional.of(properties)
            .map(ProjectProperties::getRegionSettings)
            .map(e -> e.get(gameBiz))
            .map(e -> e.get("wish-endpoint"));
    }


}
