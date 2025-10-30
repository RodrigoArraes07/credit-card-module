package org.jala.university.application.mapper;

import org.jala.university.domain.entity.User;
import org.jala.university.application.dto.UserDto;

/**
 * Mapper para converter User<-> UserDto.
 *
 * @author Hudson Silva
 * @version
 */
public class UserMapper {

    public static UserDto toDto(User user) {
        if (user == null) return null;
        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .cpf(user.getCpf())
                .phone(user.getPhone())
                .birthDate(user.getBirthDate())
                .zipCode(user.getZipCode())
                .city(user.getCity())
                .state(user.getState())
                .build();
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) return null;
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .cpf(dto.getCpf())
                .phone(dto.getPhone())
                .birthDate(dto.getBirthDate())
                .zipCode(dto.getZipCode())
                .city(dto.getCity())
                .state(dto.getState())
                .build();
    }
}
