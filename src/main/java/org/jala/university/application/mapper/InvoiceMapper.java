package org.jala.university.application.mapper;

import org.jala.university.application.dto.InvoiceCreateDTO;
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

public class InvoiceMapper implements Mapper<Invoice, InvoiceCreateDTO> {

    /**
     * Converte uma entidade Invoice em um InvoiceDto,
     * mascarando informações sensíveis como o número do cartão de crédito.
     *
     * @param input a entidade Invoice a ser convertida
     * @return InvoiceCreateDTO com dados filtrados e prontos para exposição ao usuário
     */

    private static InvoiceMapper instance;

    private InvoiceMapper() {
    }

    public static InvoiceMapper getInstance() {
        if (instance == null) {
            instance = new InvoiceMapper();
        }
        return instance;
    }

    @Override
    public InvoiceCreateDTO mapTo(Invoice input) {
        return InvoiceCreateDTO.builder()
                .closingDate(input.getClosingDate())
                .dueDate(input.getDueDate())
                .totalAmount(input.getTotalAmount())
                .isPaid(input.getIsPaid())
                .build();
    }

    @Override
    public Invoice mapFrom(InvoiceCreateDTO input) {
        return Invoice.builder()
                .closingDate(input.getClosingDate())
                .dueDate(input.getDueDate())
                .totalAmount(input.getTotalAmount())
                .isPaid(input.getIsPaid())
                .build();
    }

    public InvoiceDto toDto(Invoice input) {
        if (input == null) {
            return null;
        }
        return InvoiceDto.builder()
                .id(input.getId())
                .closingDate(input.getClosingDate())
                .dueDate(input.getDueDate())
                .totalAmount(input.getTotalAmount())
                .isPaid(input.getIsPaid())
                .build();
    }

    public Invoice fromDto(InvoiceDto input) {
        if (input == null) {
            return null;
        }
        return Invoice.builder()
                .id(input.getId())
                .closingDate(input.getClosingDate())
                .dueDate(input.getDueDate())
                .totalAmount(input.getTotalAmount())
                .isPaid(input.getIsPaid())
                .build();
    }
}
