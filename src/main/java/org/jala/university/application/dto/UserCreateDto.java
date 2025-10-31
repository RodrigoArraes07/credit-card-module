package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.time.LocalDate;
/**
 * DTO utilizado para criação de dados da entidade User.
 * Campos confidenciais e identificadores sensíveis foram omitidos.
 *
 * @author Amaury Magno
 * @version 1.0
 */
@Builder
@Getter
@Setter
@ToString
public class UserCreateDto {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String email;
    private String cpf;
    private String phone;
    private LocalDate birthDate;
    private String zipCode;
    private String city;
    private String state;
    private String password_hash;
}
