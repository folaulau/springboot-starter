package com.lovemesomecoding.dto;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import com.lovemesomecoding.user.User;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

	User userDtoToUser(UserDto dto);

	UserDto userToUserDto(User entity);

	User signupRequestToUser(SignupRequest dto);

}
