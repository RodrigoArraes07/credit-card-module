package org.jala.university.application.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jala.university.domain.enums.CreditCardStatus;

/**
 * InvoiceDto é um Data Transfer Object (DTO) utilizado para transferir apenas os dados necessários
 * da entidade Invoice, protegendo assim dados sensíveis dos usuários e facilitando a comunicação
 * entre camadas da aplicação ou com APIs externas.
 *
 * Este objeto contém somente informações relevantes que podem ser exibidas para o usuário,
 * omitindo, por exemplo, o número completo do cartão de crédito.
 *
 * @author Hudson Silva
 * @version 1.0
 */

@Builder
@Getter
@Setter
@ToString
public class InvoiceDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private LocalDate closingDate;
    private LocalDate dueDate;
    private Double totalAmount;
    private Boolean isPaid;

}