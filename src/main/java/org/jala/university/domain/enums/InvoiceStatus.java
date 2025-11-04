package org.jala.university.domain.enums;

/**
 * Enum que representa os possíveis status de uma fatura (Invoice).
 * <p>
 * Este enum define um conjunto fixo de constantes que indicam o estado atual
 * da fatura, facilitando a legibilidade e a manutenção do código.
 * O uso deste enum permite realizar verificações e filtros de forma segura e clara,
 * evitando o uso de valores literais espalhados pelo código.
 *
 * @author Hudson
 * @version 1.0
 */
public enum InvoiceStatus {
    PAID,
    UNPAID,
    CANCELED,
    OVERDUE
}
