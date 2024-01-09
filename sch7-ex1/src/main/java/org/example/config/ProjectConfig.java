package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public UserDetailsService userDetailsService(){
    var manager = new InMemoryUserDetailsManager();
    var user1 = User.withUsername("jhon")
        .password("12345").authorities("READ").build();

    var user2 = User.withUsername("jane")
        .password("12345").authorities("WRITE").build();

    var user3 = User.withUsername("kim")
        .password("12345").authorities("WRITE","READ","DELETE").build();

    manager.createUser(user1);
    manager.createUser(user2);
    manager.createUser(user3);


    return manager;
  }

  @Bean
  public PasswordEncoder passwordEncoder(){
    return NoOpPasswordEncoder.getInstance();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception{
    http.httpBasic();

//    http.authorizeRequests().anyRequest().permitAll();// 사용자의 Authorization 에 관계없이 컨트롤러 호출이 가능

//    http.authorizeRequests().anyRequest().hasAuthority("WRITE");// 사용자의 Authorization 가 WRITE면 컨트롤러 호출 가능

//    http.authorizeRequests().anyRequest().hasAnyAuthority("WRITE","READ");// 사용자의 Authorization 가 WRITE면 컨트롤러 호출 가능

    String expression =
        "hasAuthority('READ') and ! hasAuthority('DELETE')";

    http.authorizeRequests().anyRequest().access(expression);

  }

}
