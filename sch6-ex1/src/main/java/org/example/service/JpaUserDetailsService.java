package org.example.service;

import org.example.entities.User;
import org.example.model.CustomUserDetails;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.function.Supplier;

public class JpaUserDetailsService implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  @Override
  public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Supplier <UsernameNotFoundException> s =
        () -> new UsernameNotFoundException("Problem during authentication!");
    User u = userRepository.findUserByUsername(username).orElseThrow(s);

    return new CustomUserDetails(u);
  }
}
