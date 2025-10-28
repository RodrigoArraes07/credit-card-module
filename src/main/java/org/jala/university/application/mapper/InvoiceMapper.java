package org.jala.university.application.mapper;

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

public class InvoiceMapper {

    /**
     * Converte uma entidade Invoice em um InvoiceDto,
     * mascarando informações sensíveis como o número do cartão de crédito.
     *
     * @param invoice a entidade Invoice a ser convertida
     * @return InvoiceDto com dados filtrados e prontos para exposição ao usuário
     */

    public static InvoiceDto toDto(Invoice invoice) {
        InvoiceDto dto = new InvoiceDto();
        dto.setId(invoice.getId());
        dto.setClosingDate(invoice.getClosingDate());
        dto.setDueDate(invoice.getDueDate());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setIsPaid(invoice.getPaid());
        String cardNumber = invoice.getCreditCard().getNumber();
        dto.setCreditCardLast4(cardNumber.substring(cardNumber.length() - 4));
        return dto;
    }
}
