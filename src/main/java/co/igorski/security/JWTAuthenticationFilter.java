package co.igorski.security;

import co.igorski.model.AuthResponse;
import co.igorski.model.BankId;
import co.igorski.model.CollectResponse;
import co.igorski.services.bankId.BankIdException;
import co.igorski.services.bankId.BankIdService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

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


            if(bankId.getRefId() == null) {
                AuthResponse authResponse = bankIdService.auth(bankId.getBankId(), "0.0.0.0");
                res.addHeader("refId", authResponse.getOrderRef());
                return null;
            } else {

                CollectResponse collectResponse = bankIdService.collect(bankId.getRefId());
                if(collectResponse != null && "complete".equals(collectResponse.getStatus())) {
                    String personalNumber = collectResponse.getCompletitionData().getUser().getPersonalNumber();

                    bankId.setBankId(personalNumber);

                    BankIdAuthenticationToken authentication = new BankIdAuthenticationToken(bankId, new ArrayList<>());
                    bankId = bankIdService.onboard(bankId);
                    authentication.setDetails(bankId.getBankId());
                    authentication.setAuthenticated(true);

                    return authentication;
                } else {
                    throw new RuntimeException("Authentication for ref id is not complete yet.");
                }
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
            .withSubject(principal.getBankId() + "-" + principal.getId())
            .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
    }
}

