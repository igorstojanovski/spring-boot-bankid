package com.auth0.samples.authapi.springbootauthupdated.security;

import com.auth0.jwt.JWT;
import com.auth0.samples.authapi.springbootauthupdated.user.BankId;
import com.auth0.samples.authapi.springbootauthupdated.user.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.auth0.samples.authapi.springbootauthupdated.security.SecurityConstants.EXPIRATION_TIME;
import static com.auth0.samples.authapi.springbootauthupdated.security.SecurityConstants.HEADER_STRING;
import static com.auth0.samples.authapi.springbootauthupdated.security.SecurityConstants.SECRET;
import static com.auth0.samples.authapi.springbootauthupdated.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final UserDetailsServiceImpl userDetailsService;
    private AuthenticationManager authenticationManager;

    JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            BankId bankId = new ObjectMapper()
                    .readValue(req.getInputStream(), BankId.class);
            UserDetails userDetails = userDetailsService.loadUserByUsername(bankId.getBankId());

            BankIdAuthenticationToken authentication = new BankIdAuthenticationToken(
                    userDetails,
                    new ArrayList<>());
            authentication.setDetails(bankId.getBankId());
            return authenticationManager.authenticate(
                    authentication
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        String token = JWT.create()
                .withSubject(auth.getDetails() + ((UserDetails)auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}

