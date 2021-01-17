package com.lovemesomecoding.enitity.user;

import static org.springframework.http.HttpStatus.OK;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lovemesomecoding.dto.AuthenticationResponseDTO;
import com.lovemesomecoding.dto.SignUpDTO;
import com.lovemesomecoding.dto.UserDTO;
import com.lovemesomecoding.dto.UserUpdateDTO;
import com.lovemesomecoding.utils.ObjMapperUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Api(value = "users", tags = "Users")
@Slf4j
@RestController
public class UserRestController {

    @Autowired
    private UserService userService;

    /**
     * sign up
     */
    @ApiOperation(value = "Sign Up", notes = "${user.rest.controller.signup}")
    @PostMapping(value = "/users/signup")
    public ResponseEntity<AuthenticationResponseDTO> signUp(@RequestHeader(name = "x-api-key", required = true) String xApiKey,
            @ApiParam(name = "user", value = "user sign up dto", required = true) @Valid @RequestBody SignUpDTO signUpDTO) {
        log.info("sign up {}", ObjMapperUtils.toJson(signUpDTO));

        AuthenticationResponseDTO userAuthenticationSuccessDTO = userService.signUp(signUpDTO);

        return new ResponseEntity<>(userAuthenticationSuccessDTO, OK);
    }
    
    /**
     * log in
     */
    @ApiOperation(value = "Log In")
    @PostMapping(value = "/users/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestHeader(name = "x-api-key", required = true) String xApiKey,@RequestParam(name = "type", required = true) String loginType,@RequestHeader(name = "Authorization", required = true) String authorization) {
        log.info("login authorization={}", ObjMapperUtils.toJson(authorization));
        return new ResponseEntity<>(OK);
    }

    @ApiOperation(value = "Get User")
    @GetMapping(value = "/users/{uuid}")
    public ResponseEntity<UserDTO> getByUuid(@RequestHeader(name = "token", required = true) String token, @ApiParam(name = "uuid", value = "user uuid", required = true) @PathVariable String uuid) {
        log.info("getByUuid({})", uuid);

        UserDTO userDTO = userService.getByUuid(uuid);

        log.info("userDTO={}", ObjMapperUtils.toJson(userDTO));

        return new ResponseEntity<>(userDTO, OK);
    }

    @ApiOperation(value = "Update Profile")
    @PutMapping(value = "/users/profile")
    public ResponseEntity<UserDTO> updateProfile(@RequestHeader(name = "token", required = true) String token,
            @ApiParam(name = "user", value = "user profile dto", required = true) @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("updateProfile {}", ObjMapperUtils.toJson(userUpdateDTO));

        UserDTO userDTO = userService.updateProfile(userUpdateDTO);

        return new ResponseEntity<>(userDTO, OK);
    }

    @ApiOperation(value = "Update Profile Image")
    @PutMapping(value = "/users/profile-image")
    public ResponseEntity<UserDTO> updateProfileImage(@RequestHeader(name = "token", required = true) String token,
            @ApiParam(name = "uuid", value = "user uuid", required = true) @RequestParam("uuid") String uuid,
            @ApiParam(name = "file", required = true, value = "profile image") @RequestPart(value = "file") MultipartFile file) {
        log.info("updateProfileImg {}", uuid);

        UserDTO userDTO = userService.updateProfileImage(uuid, file);

        return new ResponseEntity<>(userDTO, OK);
    }

    @ApiOperation(value = "Update Cover Image")
    @PutMapping(value = "/users/cover-image")
    public ResponseEntity<UserDTO> updateCoverImage(@RequestHeader(name = "token", required = true) String token,
            @ApiParam(name = "uuid", value = "user uuid", required = true) @RequestParam("uuid") String uuid,
            @ApiParam(name = "file", required = true, value = "cover image") @RequestPart(value = "file") MultipartFile file) {
        log.info("updateProfileImg {}", uuid);

        UserDTO userDTO = userService.updateCoverImage(uuid, file);

        return new ResponseEntity<>(userDTO, OK);
    }

}
