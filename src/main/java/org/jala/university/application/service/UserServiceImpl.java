package org.jala.university.application.service;

import lombok.AllArgsConstructor;
import org.jala.university.application.dto.UserCreateDto;
import org.jala.university.application.dto.UserDto;
import org.jala.university.application.mapper.UserMapper;
import org.jala.university.domain.entity.User;
import org.jala.university.domain.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> findAll() {
        return List.of();
    }

    @Override
    public UserDto findById(UUID id) {
        return null;
    }

    @Override
    public UserDto save(UserCreateDto userCreateDto) {
        try {
            // O repositório agora cuida da criação da entidade
            User savedUser = userRepository.save(userCreateDto);

            // Converte a entidade salva para DTO
            return userMapper.mapTo(savedUser);
        } catch (IllegalStateException e) {
            // Trata o caso de CPF duplicado
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar usuário", e);
        }
    }

    @Override
    public UserDto update(UUID id, UserDto userDto) {
        return null;
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public UserDto findByCpf(String cpf) {
        User user = userRepository.findByCpf(cpf);
        return user != null ? userMapper.mapTo(user) : null;
    }
}
