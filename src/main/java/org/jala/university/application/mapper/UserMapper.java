package org.jala.university.application.mapper;

import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.User;
import org.jala.university.application.dto.UserDto;

/**
 * Mapper para converter User<-> UserDto.
 *
 * @author AmauryMagno
 * @version
 */
public class UserMapper implements Mapper<User, UserDto> {

    /**
     * Converte uma entidade User em um UserDto,
     * mascarando informações sensíveis como o senha de usuário.
     *
     * @param user a entidade User a ser convertida
     * @return UserDto com dados filtrados e prontos para exposição ao usuário
     */
    private static UserMapper instance;

    private UserMapper() {}

    public static UserMapper getInstance() {
        if (instance == null) {
            instance = new UserMapper();
        }
        return instance;
    }

    @Override
    public UserDto mapTo(User user) {
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

    @Override
    public User mapFrom(UserDto dto) {
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
