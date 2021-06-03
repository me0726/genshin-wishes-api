package com.uf.genshinwishes.config.security.oidc;

import com.uf.genshinwishes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

/**
 * @author me 2021-06-02 11:33
 */
@Component
public class GsOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final OidcUserService oidcUserService = new OidcUserService();

    @Autowired
    private UserService userService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser user = oidcUserService.loadUser(userRequest);
        if (user.getAttribute("email") == null) {
            OAuth2Error auth2Error = new OAuth2Error("No necessary permissions: email");
            throw new OAuth2AuthenticationException(auth2Error, auth2Error.toString());
        }
        return userService.loadByOidcUser(user);
    }
}
