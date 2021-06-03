package com.uf.genshinwishes.config.security.oauth2;


import com.uf.genshinwishes.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author me 2021-06-02 12:32
 */
public class GsOauth2User implements OAuth2User {

    private final OAuth2User oAuth2User;

    private final User user;

    public GsOauth2User(OAuth2User oAuth2User, User user) {
        this.oAuth2User = oAuth2User;
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return Optional.ofNullable(user.getEmail()).orElse(oAuth2User.getAttribute("email"));
    }
}
