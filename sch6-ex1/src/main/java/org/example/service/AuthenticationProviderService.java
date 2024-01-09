package org.example.service;


import org.example.model.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

public class AuthenticationProviderService implements AuthenticationProvider {
  @Autowired
  private JpaUserDetailsService userDetailsService;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private SCryptPasswordEncoder sCryptPasswordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String credential = authentication.getCredentials().toString();

    CustomUserDetails user = userDetailsService.loadUserByUsername(username);

    switch (user.getUser().getAlgorithm()) {
      case BCRYPT:
        return checkPassword(user, credential, bCryptPasswordEncoder);
      case SCRYPT:
        return checkPassword(user, credential, sCryptPasswordEncoder);
    }

    throw new BadCredentialsException("Bad credentials");
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
  }

  private Authentication checkPassword(CustomUserDetails user, String rawPassword, PasswordEncoder encoder) {
    if (encoder.matches(rawPassword, user.getPassword())) {
      return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
    } else {
      throw new BadCredentialsException("Bad credentials");
    }
  }
}