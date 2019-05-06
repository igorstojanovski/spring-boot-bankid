package com.auth0.samples.authapi.springbootauthupdated.security;

import com.auth0.samples.authapi.springbootauthupdated.services.BankIdService;
import com.auth0.samples.authapi.springbootauthupdated.user.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.auth0.samples.authapi.springbootauthupdated.security.SecurityConstants.SIGN_UP_URL;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final BankIdAuthenticationProvider authenticationProvider;
    private final BankIdService bankIdService;
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public WebSecurity(UserDetailsServiceImpl userDetailsService, BankIdAuthenticationProvider authenticationProvider,
                       BankIdService bankIdService) {
        this.userDetailsService = userDetailsService;
        this.authenticationProvider = authenticationProvider;
        this.bankIdService = bankIdService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), bankIdService))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), userDetailsService, bankIdService))

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
