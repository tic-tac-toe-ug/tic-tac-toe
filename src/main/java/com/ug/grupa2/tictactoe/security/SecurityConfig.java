package com.ug.grupa2.tictactoe.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //TODO: enable csrf protection
    http.csrf().disable()
      .authorizeRequests()
        .antMatchers("/","/users")
          .permitAll()
        .anyRequest()
          .access("hasRole('ROLE_USER')")
      .and()
        .formLogin()
          .loginPage("/login-form")
      .and()
        .logout()
          .logoutSuccessUrl("/");
  }

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth)
    throws Exception {
    auth
      .userDetailsService(userService)
      .passwordEncoder(encoder());
  }
}
