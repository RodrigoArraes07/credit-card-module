package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO utilizado para transferência de dados da entidade User.
 * Campos confidenciais e identificadores sensíveis foram omitidos.
 *
 * @author Hudson Silva
 * @version 1.0
 */
@Builder
@Getter
@Setter
@ToString
public class UserDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String name;
    private String email;
    private String cpf;
    private String phone;
    private LocalDate birthDate;
    private String zipCode;
    private String city;
    private String state;
}

