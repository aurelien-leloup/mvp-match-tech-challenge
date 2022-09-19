package com.mvpmatch.vendingmachine.services;

import com.mvpmatch.vendingmachine.daos.UserRepository;
import com.mvpmatch.vendingmachine.exceptions.UnauthorizedException;
import com.mvpmatch.vendingmachine.models.AuthentifiedUser;
import com.mvpmatch.vendingmachine.models.User;
import com.mvpmatch.vendingmachine.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;


    public AuthentifiedUser login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User user = userRepository.findById(username).orElseThrow();
            String token = jwtTokenProvider.createToken(username, user.getRole());
            return new AuthentifiedUser(user, token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new UnauthorizedException();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findById(username).orElseThrow();

        return org.springframework.security.core.userdetails.User//
                .withUsername(username)//
                .password(user.getPassword())//
                .authorities(user.getRole())//
                .accountExpired(false)//
                .accountLocked(false)//
                .credentialsExpired(false)//
                .disabled(false)//
                .build();
    }

    public UserDetails getAuthUser() {
        AbstractAuthenticationToken auth = (AbstractAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) auth.getDetails();
    }


}
