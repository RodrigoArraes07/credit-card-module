package org.jala.university.application.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceCreateDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public LocalDate closingDate;
    public LocalDate dueDate;
    public Double totalAmount;
    public Boolean isPaid;
}
