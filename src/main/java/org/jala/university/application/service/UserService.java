package org.jala.university.application.service;

import org.jala.university.application.dto.UserCreateDto;
import org.jala.university.application.dto.UserDto;
import org.jala.university.domain.entity.User;

import java.util.List;
import java.util.UUID;
public interface UserService {
    List<UserDto> findAll();
    UserDto findById(UUID id);
    UserDto save(UserCreateDto userCreateDto);
    UserDto update(UUID id, UserDto userDto);
    void deleteById(UUID id);
    UserDto findByCpf(String cpf);
    UserDto authenticate(String cpf, String password);
}
