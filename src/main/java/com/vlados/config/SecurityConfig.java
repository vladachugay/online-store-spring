package com.vlados.config;

import com.vlados.entity.Role;
import com.vlados.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                    .authorizeRequests()
                .antMatchers("/orders/create", "/orders/create", "/user", "/logout")
                    .hasAnyAuthority(Role.ROLE_USER.getAuthority(), Role.ROLE_ADMIN.getAuthority())
                .antMatchers("/orders/changestatus/{id}", "/products/add", "/products/edit/{id}",
                        "/products/delete", "/user/lock/{id}", "/user/unlock/{id}", "/users", "/admin")
                    .hasAuthority(Role.ROLE_ADMIN.getAuthority())
                .antMatchers("/css/**", "/", "/cart/**", "/products", "/products/{id}")
                    .permitAll()
                .antMatchers("/login", "/registration").anonymous()
                    .and()
                .formLogin()
                .loginPage("/login")
//                .failureUrl("login-error")
                .successHandler(myAuthenticationSuccessHandler());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return new UrlAuthenticationSuccessHandler();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}
