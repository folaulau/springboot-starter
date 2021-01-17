package com.lovemesomecoding.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.TimeZone;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.lovemesomecoding.dto.AuthenticationResponseDTO;
import com.lovemesomecoding.dto.EntityDTOMapperImpl;
import com.lovemesomecoding.dto.SignUpDTO;
import com.lovemesomecoding.enitity.user.User;
import com.lovemesomecoding.enitity.user.UserDAO;
import com.lovemesomecoding.enitity.user.UserService;
import com.lovemesomecoding.enitity.user.UserServiceImp;
import com.lovemesomecoding.entity.user.role.Authority;
import com.lovemesomecoding.entity.user.role.Role;
import com.lovemesomecoding.exception.ApiErrorResponse;
import com.lovemesomecoding.exception.ApiException;
import com.lovemesomecoding.security.AuthenticationService;
import com.lovemesomecoding.utils.ObjMapperUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class SignupMockTests {

    @InjectMocks
    private UserService           userService = new UserServiceImp();

    @Mock
    private UserDAO               userDAO;

    @Mock
    private AuthenticationService authenticationService;

    @Captor
    ArgumentCaptor<User>          savedUserCaptor;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(userService, "entityMapper", new EntityDTOMapperImpl());
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void test_signup_user_invalid_payload() {

        ApiException apiException = Assertions.assertThrows(ApiException.class, () -> {
            userService.signUp(null);
        });

        ApiErrorResponse error = apiException.getError();

        assertThat(error).isNotNull();

        assertThat(error.getMessage()).isNotNull().isEqualTo("Something went wrong. Please try again.");

    }

    @Test
    public void test_signup_user_invalid_email() {
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setEmail("folau");
        signUpDTO.setPassword("Test1234!");

        ApiException apiException = Assertions.assertThrows(ApiException.class, () -> {
            userService.signUp(signUpDTO);
        });

        ApiErrorResponse error = apiException.getError();

        assertThat(error).isNotNull();

        assertThat(error.getMessage()).isNotNull().isEqualTo("Email is invalid");

    }

    @Test
    public void test_signup_user_invalid_password() {
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setEmail("folaudev@gmail.com");
        signUpDTO.setPassword("test");

        ApiException apiException = Assertions.assertThrows(ApiException.class, () -> {
            userService.signUp(signUpDTO);
        });

        ApiErrorResponse error = apiException.getError();

        assertThat(error).isNotNull();

        assertThat(error.getMessage()).isNotNull().isEqualTo("Password is invalid");

    }

    @Test
    public void test_signup_user_successful() {
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setEmail("folaudev@gmail.com");
        signUpDTO.setPassword("Test1234!");

        Mockito.when(userDAO.save(Mockito.any(User.class))).thenReturn(new User());

        Mockito.when(authenticationService.authenticate(Mockito.any(User.class))).thenReturn(new AuthenticationResponseDTO());

        userService.signUp(signUpDTO);

        Mockito.verify(userDAO).save(savedUserCaptor.capture());

        User savedUser = savedUserCaptor.getValue();

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isNotNull().isEqualTo("folaudev@gmail.com");
        assertThat(savedUser.getRoles()).isNotNull();

        Optional<Role> optRole = savedUser.getRoles().stream().findFirst();

        assertThat(optRole.isPresent()).isTrue();
        assertThat(optRole.get().getAuthority()).isEqualTo(Authority.USER);

        assertThat(savedUser.getPassword()).isNotNull();

    }

}
