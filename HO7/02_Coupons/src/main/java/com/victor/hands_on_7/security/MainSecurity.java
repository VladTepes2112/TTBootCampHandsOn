package com.victor.hands_on_7.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class MainSecurity extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passer;

    @Autowired
    public MainSecurity(PasswordEncoder passer) {
        this.passer = passer;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/coupon/all").permitAll()
            .antMatchers("/coupon/byid/*").permitAll()
            .antMatchers("/coupon/add").hasRole("ADMIN")
            .antMatchers("/coupon/discount/bycode/*").hasRole("CUSTOMER")
            .antMatchers("/coupon/delete/*").hasRole("ADMIN")
            .anyRequest().authenticated().and().httpBasic();
        ;
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.builder().username("vinoth").password(passer.encode("12345")).roles("ADMIN", "CUSTOMER").build();
        UserDetails customer1 = User.builder().username("karen").password(passer.encode("trump2020")).roles("CUSTOMER").build();
        return new InMemoryUserDetailsManager(admin, customer1);
    }
}
/*
.authorizeRequests()
        .antMatchers("/ping**")
        .permitAll()
        .and()
.authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
 */