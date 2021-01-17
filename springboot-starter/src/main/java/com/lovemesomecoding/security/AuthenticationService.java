package com.lovemesomecoding.security;

import com.lovemesomecoding.dto.AuthenticationResponseDTO;
import com.lovemesomecoding.enitity.user.User;

public interface AuthenticationService {

    AuthenticationResponseDTO authenticate(User user);

}
