package org.jala.university.domain.repository;

import org.jala.university.application.dto.UserCreateDto;
import org.jala.university.commons.domain.repository.Repository;
import org.jala.university.domain.entity.User;

import java.util.UUID;

public interface UserRepository extends Repository<User, UUID> {
    User findByCpf(String cpf);
    User save(UserCreateDto user);

}
