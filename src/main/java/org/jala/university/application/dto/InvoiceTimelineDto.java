package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@ToString
public class InvoiceTimelineDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private InvoiceDetailDto previousInvoice;
    private InvoiceDetailDto currentInvoice;
    private InvoiceDetailDto nextInvoice;
    private Integer year;
}
