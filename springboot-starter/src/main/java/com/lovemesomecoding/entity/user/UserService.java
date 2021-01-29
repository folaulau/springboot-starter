package com.lovemesomecoding.entity.user;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.lovemesomecoding.dto.AuthenticationResponseDTO;
import com.lovemesomecoding.dto.SignUpDTO;
import com.lovemesomecoding.dto.UserDTO;
import com.lovemesomecoding.dto.UserUpdateDTO;

public interface UserService {

    AuthenticationResponseDTO signUp(SignUpDTO signUpDTO);

    UserDTO getByUuid(String uuid);

    UserDTO updateProfileImage(String uuid, MultipartFile file);

    UserDTO updateProfile(UserUpdateDTO userUpdateDTO);

    UserDTO updateCoverImage(String uuid, MultipartFile file);

    void sendMonthlyPaymentReminder(String userUuid);

}
