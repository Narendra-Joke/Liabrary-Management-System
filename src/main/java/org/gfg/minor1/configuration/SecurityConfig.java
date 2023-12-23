package org.gfg.minor1.configuration;

import org.gfg.minor1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Value("${admin.authority}")
    private String adminAuthority;

    @Value("${student.authority}")
    private String studentAuthority;

    // this is for authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userService);
    }

    // this is for authorization
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable().httpBasic().and().authorizeRequests().
                antMatchers("/admin/create").permitAll().
                antMatchers("/book/create").hasAuthority(adminAuthority).
                antMatchers("/book/find","/student/create","/student/find").hasAnyAuthority(studentAuthority,adminAuthority).
                antMatchers("/txn/create","/txn/return").hasAuthority(adminAuthority).
                antMatchers("/**").permitAll().and().formLogin();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
