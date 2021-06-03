package com.uf.genshinwishes.config.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author me 2021-06-02 17:59
 */
@Component
public class SecurityConfigHandler implements AuthenticationEntryPoint, AccessDeniedHandler, LogoutSuccessHandler, AuthenticationSuccessHandler {

    private final HttpStatusEntryPoint point = new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);

    private final AuthenticationSuccessHandler authenticationSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        point.commence(request, response, authException);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
//TODO
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//       认证成功之后可以签发token/
        authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
    }

}
