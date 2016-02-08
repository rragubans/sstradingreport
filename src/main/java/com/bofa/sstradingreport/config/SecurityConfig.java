
package com.bofa.sstradingreport.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile({"default", "prod"})
public class SecurityConfig {

    /**
     * When using Spring Boot Dev Tools,
     * {@code SecurityProperties.BASIC_AUTH_ORDER - 20} will already be used for
     * the h2 web console if that hasn't been explicitly disabled.
     */
    @Configuration
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 20)
    @ConditionalOnBean(SecurityConfig.class)
    protected static class ApplicationWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            http
                    .httpBasic()
                    .and()
                    .authorizeRequests()
                    .antMatchers(
                            "/api/system/env/java.(runtime|vm).*",
                            "/api/system/metrics/**"
                    ).permitAll()
                    .antMatchers("/api/system/env/**").denyAll()
                    .antMatchers("/**").permitAll()
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(STATELESS)
                    .and()
                    .csrf()
                    .disable()
                    .headers()
                    .frameOptions() // OEmbedController#embedTrack uses an iframe
                    .disable()
            ;
        }
    }
}