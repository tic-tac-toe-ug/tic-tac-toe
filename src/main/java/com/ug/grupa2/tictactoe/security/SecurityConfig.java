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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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
      .successHandler(successHandler(this.mapper))
      .and()
      .logout()
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

  private AuthenticationSuccessHandler successHandler(ObjectMapper mapper) {
    return (httpServletRequest, httpServletResponse, authentication) -> {
      // password should not be sent
      httpServletResponse.getWriter().write(mapper.writeValueAsString(authentication));
      httpServletResponse.setContentType("application/json");
      httpServletResponse.setStatus(200);
    };
  }
}
