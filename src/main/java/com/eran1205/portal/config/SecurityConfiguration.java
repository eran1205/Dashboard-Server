package com.eran1205.portal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        //httpSecurity.authorizeRequests().antMatchers("/").permitAll();

        httpSecurity
            .authorizeRequests()
                .antMatchers("/api/*", "/api/*/*").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable()
//            .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .and()
            .logout()
                .permitAll();
    }
}