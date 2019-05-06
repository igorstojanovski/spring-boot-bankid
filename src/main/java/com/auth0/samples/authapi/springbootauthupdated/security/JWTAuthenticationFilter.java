package com.auth0.samples.authapi.springbootauthupdated.security;

import com.auth0.jwt.JWT;
import com.auth0.samples.authapi.springbootauthupdated.model.AuthResponse;
import com.auth0.samples.authapi.springbootauthupdated.model.BankId;
import com.auth0.samples.authapi.springbootauthupdated.model.CollectResponse;
import com.auth0.samples.authapi.springbootauthupdated.services.BankIdException;
import com.auth0.samples.authapi.springbootauthupdated.services.BankIdService;
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
    private final BankIdService bankIdService;
    private AuthenticationManager authenticationManager;

    JWTAuthenticationFilter(AuthenticationManager authenticationManager, BankIdService bankIdService) {
        this.authenticationManager = authenticationManager;
        this.bankIdService = bankIdService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            BankId bankId = new ObjectMapper().readValue(req.getInputStream(), BankId.class);
            BankIdAuthenticationToken authentication = new BankIdAuthenticationToken(bankId, new ArrayList<>());

            if(bankId.getRefId() == null) {
                AuthResponse authResponse = bankIdService.auth(bankId.getBankId(), "0.0.0.0");
                res.addHeader("refId", authResponse.getOrderRef());
                return null;
            } else {
                CollectResponse collectResponse = bankIdService.collect(bankId.getRefId());
                if(collectResponse != null && "complete".equals(collectResponse.getStatus())) {
                    bankId = bankIdService.onboard(bankId);
                    authentication.setDetails(bankId.getBankId());
                    authentication.setAuthenticated(true);

                    return authentication;
                }

                return null;
            }
        } catch (IOException | BankIdException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) {
        BankId principal = (BankId) auth.getPrincipal();
        String token = JWT.create()
                .withSubject(principal.getBankId() + principal.getId())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}

