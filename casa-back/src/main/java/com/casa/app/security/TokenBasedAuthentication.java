//package com.casa.app.security;
//
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//
////import java.io.Serial;
//
//
//public class TokenBasedAuthentication extends AbstractAuthenticationToken {
////    @Serial
////    private static final long serialVersionUID = 7400177908078971661L;
//    private String token;
//    private final UserDetails principle;
//
//    public TokenBasedAuthentication(UserDetails principle) {
//        super(principle.getAuthorities());
//        this.principle = principle;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    @Override
//    public boolean isAuthenticated() {
//        return true;
//    }
//
//    @Override
//    public Object getCredentials() {
//        return token;
//    }
//
//    @Override
//    public UserDetails getPrincipal() {
//        return principle;
//    }
//}