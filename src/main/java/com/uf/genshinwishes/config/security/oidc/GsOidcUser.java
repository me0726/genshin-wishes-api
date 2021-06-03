package com.uf.genshinwishes.config.security.oidc;


import com.uf.genshinwishes.config.security.oauth2.GsOauth2User;
import com.uf.genshinwishes.model.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Map;

/**
 * @author me 2021-06-02 12:44
 */
public class GsOidcUser extends GsOauth2User implements OidcUser {

    private final OidcUser oidcUser;

    public GsOidcUser(OidcUser oidcUser, User user) {
        super(oidcUser, user);
        this.oidcUser = oidcUser;
    }

    @Override
    public Map<String, Object> getClaims() {
        return oidcUser.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser.getIdToken();
    }
}
