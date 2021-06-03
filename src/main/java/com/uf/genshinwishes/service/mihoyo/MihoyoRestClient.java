package com.uf.genshinwishes.service.mihoyo;

import com.uf.genshinwishes.dto.mihoyo.MihoyoWishDataDTO;
import com.uf.genshinwishes.dto.mihoyo.MihoyoWishLogDTO;
import com.uf.genshinwishes.dto.mihoyo.MihoyoWishRetDTO;
import com.uf.genshinwishes.exception.ApiError;
import com.uf.genshinwishes.exception.ErrorType;
import com.uf.genshinwishes.model.enums.BannerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class MihoyoRestClient {
    private final Logger logger = LoggerFactory.getLogger(MihoyoRestClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MihoyoGameBizSettingsSelector selector;

    public List<MihoyoWishLogDTO> getWishes(String authkey, String gameBiz, BannerType banner, String lastWishId, Integer page) throws ApiError {
        String url = selector.getWishEndpoint(gameBiz).orElseThrow(() -> new ApiError(ErrorType.NO_SUITABLE_ENDPOINT_FOR_GAME_BIZ));
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/event/gacha_info/api/getGachaLog")
            .queryParam("authkey", authkey)
            .queryParam("init_type", banner.getType())
            .queryParam("gacha_type", banner.getType())
            .queryParam("authkey_ver", 1)
            .queryParam("sign_type", 2)
            .queryParam("auth_appid", "webview_gacha")
            .queryParam("lang", "en")
            .queryParam("size", 20)
            .queryParam("page", page)
            .queryParam("end_id", lastWishId);
        try {
            URI uri = builder.build(true).toUri();
            MihoyoWishRetDTO ret = restTemplate.getForEntity(uri, MihoyoWishRetDTO.class).getBody();
            return Optional.ofNullable(ret).map(MihoyoWishRetDTO::getData)
                .map(MihoyoWishDataDTO::getList)
                .orElseThrow(() -> new ApiError(ErrorType.AUTHKEY_INVALID));
        } catch (RestClientException e) {
            logger.error("Can't import wishes from mihoyo", e);
            throw new ApiError(ErrorType.MIHOYO_UNREACHABLE);
        }

    }

}
