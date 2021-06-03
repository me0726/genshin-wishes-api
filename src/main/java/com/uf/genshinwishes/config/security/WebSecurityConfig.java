package com.uf.genshinwishes.config.security;

import com.uf.genshinwishes.config.security.oauth2.GsOAuth2UserService;
import com.uf.genshinwishes.config.security.oidc.GsOidcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author me 2021-06-01 10:44
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    private GsOAuth2UserService oAuth2UserService;

    @Autowired
    private GsOidcUserService oidcUserService;

    @Autowired
    private SecurityConfigHandler securityConfigHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
            .cors()
            .and()
            .csrf()
            .and()
            .authorizeRequests()
            .antMatchers("/oauth2/**",
                "/logout",
                "/error",
                "/login/**",
                "/user",
                "/items/**",
                "/actuator/**",
                "/public/**",
                "/profile/**")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .oauth2Login()
            .userInfoEndpoint()
            .oidcUserService(oidcUserService)
            .userService(oAuth2UserService)
            .and()
            .tokenEndpoint()
            .and()
            .successHandler(securityConfigHandler)
            .and()
            .exceptionHandling()
            .defaultAuthenticationEntryPointFor(securityConfigHandler, new AntPathRequestMatcher("/**"))
            .and()
            .logout()
            .permitAll()
            .logoutSuccessHandler(securityConfigHandler);

    }

}
