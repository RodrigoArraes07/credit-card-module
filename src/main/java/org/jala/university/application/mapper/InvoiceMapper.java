package org.jala.university.application.mapper;

import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.Invoice;
import org.jala.university.application.dto.InvoiceDto;

/**
 * InvoiceMapper é uma classe utilitária responsável por converter objetos do tipo Invoice (entidade)
 * em InvoiceDto (DTO) e vice-versa, centralizando a lógica de transformação dos dados.
 *
 * Esse mapper garante que apenas as informações adequadas sejam repassadas para o DTO,
 * como, por exemplo, o mascaramento do número completo do cartão de crédito, que não é para ser visivel para o usuario.
 *
 * @author Hudson
 * @version 1.0
 */

public class InvoiceMapper implements Mapper<Invoice, InvoiceDto> {

    /**
     * Converte uma entidade Invoice em um InvoiceDto,
     * mascarando informações sensíveis como o número do cartão de crédito.
     *
     * @param input a entidade Invoice a ser convertida
     * @return InvoiceDto com dados filtrados e prontos para exposição ao usuário
     */
    @Override
    public InvoiceDto mapTo(Invoice input) {
        return InvoiceDto.builder()
                .id(input.getId())
                .closingDate(input.getClosingDate())
                .dueDate(input.getDueDate())
                .totalAmount(input.getTotalAmount())
                .isPaid(input.getIsPaid())
                .build();
    }

    @Override
    public Invoice mapFrom(InvoiceDto input) {
        return Invoice.builder()
                .id(input.getId())
                .closingDate(input.getClosingDate())
                .dueDate(input.getDueDate())
                .totalAmount(input.getTotalAmount())
                .isPaid(input.getIsPaid())
                .build();
    }
}
