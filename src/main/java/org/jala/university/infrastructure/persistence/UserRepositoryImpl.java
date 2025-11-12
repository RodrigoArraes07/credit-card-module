package org.jala.university.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.jala.university.application.dto.UserCreateDto;
import org.jala.university.application.dto.UserDto;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.User;
import org.jala.university.domain.repository.UserRepository;
import org.jala.university.infrastructure.util.PasswordHashUtil;

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
        EntityManager em = getEntityManager();

        try {
            User existingUser = findByCpf(userCreateDto.getCpf());
            if (existingUser != null) {
                throw new IllegalStateException("Já existe um usuário cadastrado com este CPF");
            }

            String hashedPassword = PasswordHashUtil.hashPassword(userCreateDto.getPassword_hash());

            User user = User.builder()
                    .name(userCreateDto.getName())
                    .email(userCreateDto.getEmail())
                    .cpf(userCreateDto.getCpf())
                    .passwordHash(hashedPassword)
                    .phone(userCreateDto.getPhone())
                    .birthDate(userCreateDto.getBirthDate())
                    .zipCode(userCreateDto.getZipCode())
                    .city(userCreateDto.getCity())
                    .state(userCreateDto.getState())
                    .build();

            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();

            return user;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao salvar usuário: " + e.getMessage(), e);
        }
    }

}
