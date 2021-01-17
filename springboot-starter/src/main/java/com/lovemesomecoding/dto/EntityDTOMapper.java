package com.lovemesomecoding.dto;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import com.lovemesomecoding.enitity.user.User;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EntityDTOMapper {

    User mapSignUpDTOToUser(SignUpDTO signUpDTO);

    User mapUserUpdateDTOToUser(UserUpdateDTO userUpdateDTO);

    @Mappings({@Mapping(target = "id", ignore = true), @Mapping(target = "uuid", ignore = true), @Mapping(target = "address", ignore = true)})
    User patchUpdateUser(UserUpdateDTO userUpdateDTO, @MappingTarget User user);

    UserDTO mapUserToUserDTO(User user);

    AuthenticationResponseDTO mapUserToUserAuthSuccessDTO(User user);

}
