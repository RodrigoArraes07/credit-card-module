package org.jala.university.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.jala.university.application.dto.UserCreateDto;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.User;
import org.jala.university.domain.repository.UserRepository;

import java.util.UUID;
/**
 * Camada de execucao para consultas.
 *
 * @author Amaury Magno
 * @version 1.0
 */
public class UserRepositoryImpl
        extends CrudRepository<User, UUID> implements UserRepository{

    public UserRepositoryImpl(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    @Override
    public User findByCpf(String cpf) {
        try {
            String jpql = "SELECT u FROM User u WHERE u.cpf = :cpf";
            TypedQuery<User> query = getEntityManager().createQuery(jpql, User.class);
            query.setParameter("cpf", cpf);
            return query.getSingleResult();
        }catch (NoResultException e){
            return null;
        }

    }

    @Override
    public User save(UserCreateDto userCreateDto) {
        try {
            EntityManager em = getEntityManager();

            User user = User.builder()
                    .id(UUID.randomUUID())
                    .name(userCreateDto.getName().toLowerCase())
                    .email(userCreateDto.getEmail().toLowerCase())
                    .cpf(userCreateDto.getCpf())
                    .phone(userCreateDto.getPhone())
                    .birthDate(userCreateDto.getBirthDate())
                    .zipCode(userCreateDto.getZipCode())
                    .city(userCreateDto.getCity())
                    .state(userCreateDto.getState())
                    .build();

            // Verifica se já existe um usuário com o mesmo CPF
            if (findByCpf(user.getCpf()) != null) {
                throw new IllegalStateException("Já existe um usuário cadastrado com este CPF");
            }

            em.merge(user);
            em.flush(); // Força o flush para garantir que o ID seja gerado

            return user;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar usuário: " + e.getMessage(), e);
        }
    }


}
