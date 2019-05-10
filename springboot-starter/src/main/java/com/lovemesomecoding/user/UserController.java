package com.lovemesomecoding.user;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lovemesomecoding.dto.SessionDTO;
import com.lovemesomecoding.dto.SignupRequest;
import com.lovemesomecoding.dto.UserDto;
import com.lovemesomecoding.dto.UserMapper;
import com.lovemesomecoding.jwt.JwtPayload;
import com.lovemesomecoding.role.Role;
import com.lovemesomecoding.utils.HttpUtils;
import com.lovemesomecoding.utils.ObjectUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "users",produces = "Rest API for user operations", tags = "User Controller")
@RestController
public class UserController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserService userService;
	
	
	@Autowired
	private UserMapper userMapper;
	
	/**
	 * 
	 * @param apiKey
	 * @param user
	 * @return
	 */
	
	@ApiOperation(value = "Sign Up")
	@PostMapping("/signup")
	public ResponseEntity<SessionDTO> signUp(@ApiParam(name="user", required=true, value="user") @Valid @RequestBody SignupRequest signupRequest){
		log.debug("signUp(..)");
		SessionDTO userSession = userService.signUp(signupRequest);
		log.debug("userSession: {}",ObjectUtils.toJson(userSession));
		return new ResponseEntity<>(userSession, HttpStatus.OK);
	}
	
	/**
	 * This method is for show only. It does not get called on login.
	 * Check CustomLoginFilter.java - Spring security set this up.
	 * 
	 * @param apiKey
	 * @param user
	 * @return
	 */
	
	@ApiOperation(value = "Login")
	@PostMapping("/login")
	public ResponseEntity<SessionDTO> login(@ApiParam(name="authorization", required=true, value="Base64 username and password encoded token") @RequestHeader("authorization") String authorization){
		log.info("login(...)");
		return new ResponseEntity<>(new SessionDTO(), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Logout")
	@DeleteMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader("token") String token){
		log.info("logout(...)");
		
		ObjectNode result = ObjectUtils.getObjectNode();
		result.put("status", "good");
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get Basic Auth Token")
	@PostMapping("/generate-basic-auth-token")
	public ResponseEntity<ObjectNode> generateBasicAuthToken(@RequestParam("email") String email,@RequestParam("password") String password ) {
		log.info("generateBasicAuthToken");
		ObjectNode auth = ObjectUtils.getObjectNode();
		String authToken = HttpUtils.generateBasicAuthenticationToken(email, password);
		auth.put("token", authToken);
		return new ResponseEntity<>(auth, HttpStatus.OK);
	}
	
	@Secured(value={Role.USER})
	@ApiOperation(value = "Get Member By Uuid")
	@GetMapping("/users/{uid}")
	public ResponseEntity<UserDto> getUserByUid(@RequestHeader(name="token", required=true) String token, @ApiParam(name="uid", required=true, value="uid") @PathVariable("uid") String uid){
		log.debug("getUserByUid(..)");
		
		User user = userService.getByUid(uid);
		
		UserDto userDto = userMapper.userToUserDto(user);
		
		log.debug("userDto: {}",ObjectUtils.toJson(userDto));
		
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}
}
