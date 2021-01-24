package com.ug.grupa2.tictactoe.security;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userService;
  private final ObjectMapper mapper;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //TODO: enable csrf protection, do we need to?
    http.csrf().disable()
      .authorizeRequests()
      .antMatchers("/", "/*.js*", "/login", "/users")
      .permitAll()
      .anyRequest()
      .authenticated()
      .and()
      .formLogin()
      .successHandler(successHandler())
      .failureHandler(failureHandler())
      .and()
      .logout()
      .logoutSuccessHandler(logoutHandler())
      .deleteCookies("JSESSIONID");
  }

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth)
    throws Exception {
    auth
      .userDetailsService(this.userService)
      .passwordEncoder(encoder());
  }

  private AuthenticationSuccessHandler successHandler() {
    return (request, response, authentication) -> {
      // password should not be sent
      response.getWriter().write(mapper.writeValueAsString(authentication));
      response.setContentType("application/json");
      response.setStatus(200);
    };
  }

  private AuthenticationFailureHandler failureHandler() {
    return (request, response, e) -> {
      final Map<String, String> errorModel = new HashMap<>();
      response.setContentType("application/json; charset=utf-8");
      errorModel.put("errors", "Podano błędne dane logowania");
      response.getWriter().write(mapper.writeValueAsString(errorModel));
      response.setStatus(400);
    };
  }

  private LogoutSuccessHandler logoutHandler() {
    return (request, response, authentication) -> {
      final Map<String, Boolean> logoutModel = new HashMap<>();
      logoutModel.put("logout", true);
      response.getWriter().write(mapper.writeValueAsString(logoutModel));
      response.setStatus(200);
      response.setContentType("application/json");
    };
  }
}
