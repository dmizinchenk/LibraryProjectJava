package ru.Zinchenko.LibraryProject.security.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.Zinchenko.LibraryProject.security.securityUser.UserDetailsServiceImplementation;

//@Component
public class AuthProviderImplement implements AuthenticationProvider {
    private final UserDetailsServiceImplementation service;
//    private final PasswordEncoder encoder = new BCryptPasswordEncoder(12);

//    @Autowired
    public AuthProviderImplement(UserDetailsServiceImplementation service) {
        this.service = service;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        UserDetails userDetail = service.loadUserByUsername(login);

//        String password = encoder.encode(authentication.getCredentials().toString());
        String password = authentication.getCredentials().toString();
        if(!password.equals(userDetail.getPassword()))
            throw new BadCredentialsException("Incorrect password");

        return new UsernamePasswordAuthenticationToken(userDetail, password, userDetail.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
