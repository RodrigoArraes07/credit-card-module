package org.jala.university.application.dto;

import java.time.LocalDate;
import java.util.UUID;
import org.jala.university.domain.entity.CreditCardStatus;

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

public class InvoiceDto {
    private UUID id;
    private LocalDate closingDate;
    private LocalDate dueDate;
    private Double totalAmount;
    private Boolean isPaid;
    private String creditCardLast4;        // ultimos numero dos cartão
    private CreditCardStatus creditCardStatus;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Boolean getIsPaidPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean paid) {
        isPaid = paid;
    }

    public String getCreditCardLast4() {
        return creditCardLast4;
    }

    public void setCreditCardLast4(String creditCardLast4) {
        this.creditCardLast4 = creditCardLast4;
    }

    public CreditCardStatus getCreditCardStatus() {
        return creditCardStatus;
    }

    public void setCreditCardStatus(CreditCardStatus creditCardStatus) {
        this.creditCardStatus = creditCardStatus;
    }
}