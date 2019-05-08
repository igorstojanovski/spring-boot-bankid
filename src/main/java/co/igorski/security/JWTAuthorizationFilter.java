package co.igorski.security;

import co.igorski.services.bankId.BankIdService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final BankIdService bankIdService;

    @Autowired
    public JWTAuthorizationFilter(AuthenticationManager authManager, BankIdService bankIdService) {
        super(authManager);
        this.bankIdService = bankIdService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(SecurityConstants.HEADER_STRING);

        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(req));
        chain.doFilter(req, res);
    }

    private BankIdAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER_STRING);
        if (token != null) {
            String uniqueId = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
                    .build()
                .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                    .getSubject();

            if (uniqueId != null) {
                String bankId = uniqueId.split("-")[0];
                if (bankIdService.isValid(bankId)) {
                    return new BankIdAuthenticationToken(bankIdService.find(bankId), new ArrayList<>());
                }
            }
            return null;
        }
        return null;
    }
}

