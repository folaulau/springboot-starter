package com.lovemesomecoding.security;

import com.lovemesomecoding.dto.AuthenticationResponseDTO;
import com.lovemesomecoding.enitity.user.User;
import com.lovemesomecoding.security.jwt.JwtPayload;

public interface AuthenticationService {

    AuthenticationResponseDTO authenticate(User user);

    boolean logOutUser(String token);

    boolean authorizeRequest(String token, JwtPayload jwtPayload);

}
