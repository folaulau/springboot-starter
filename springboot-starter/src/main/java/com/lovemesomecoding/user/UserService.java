package com.lovemesomecoding.user;

import java.util.Optional;

import com.lovemesomecoding.dto.SessionDTO;
import com.lovemesomecoding.dto.SignupRequest;

public interface UserService {
	
	User create(User user);
	
	User getById(Long id);
	
	User getProfileById(Long id);
	
	User getByUid(String uid);
	
	Optional<User> findByUid(String uid);

	Optional<User> findByEmail(String email);
	
	User getByEmail(String email);

	SessionDTO signUp(SignupRequest signupRequest);
}
