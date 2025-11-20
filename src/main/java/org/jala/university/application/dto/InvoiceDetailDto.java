package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jala.university.domain.enums.PaymentMethod;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
public class InvoiceDetailDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private LocalDate closingDate;
    private LocalDate dueDate;
    private Double totalAmount;
    private Double lateFee;
    private Double interestCharges;
    private Boolean isPaid;
    private PaymentMethod paymentMethod;
    private LocalDateTime paymentDate;
    private List<PurchaseInstallmentDto> installments;
}
