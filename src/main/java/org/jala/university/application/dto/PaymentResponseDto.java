package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jala.university.domain.enums.PaymentMethod;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
public class PaymentResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Boolean success;
    private String message;
    private UUID invoiceId;
    private PaymentMethod paymentMethod;
    private LocalDateTime paymentDate;
}