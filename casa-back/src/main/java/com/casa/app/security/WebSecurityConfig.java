package com.casa.app.security;

import com.casa.app.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.regex.Pattern;

import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig  {
    private final AuthenticationProvider authenticationProvider;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;



    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.authorizeHttpRequests().requestMatchers("/*").permitAll().anyRequest().authenticated();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);
        http.headers().frameOptions().disable();
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
                .ignoring()
                .requestMatchers(HttpMethod.POST, "/api/login", "/api/register",
                        "/api/device/register",
                        "/h2-console/**")
                .requestMatchers(HttpMethod.GET,
                        "/webjars/**",
                        "/*.html", "favicon.ico",
//                        "/**/*.html", "/**/*.css", "/**/*.js",
                        "/h2-console/**",
                        "/api/realEstateRequest/getAll",
                        "/api/verify",
//                      CAREFUL
//                      "/api/user/{id}" conflicts with for example "/api/user/init where auth is required,
//                      so add public to non auth enpoint somewhere
                        "/api/location/getAllCountries", "/api/location/getAllCities/{country}"
                        )
                .requestMatchers(new RegexRequestMatcher(".*/api.*/public.*", "GET"))
                .requestMatchers(HttpMethod.PATCH, "/api/realEstateRequest/manage");

    }
}

