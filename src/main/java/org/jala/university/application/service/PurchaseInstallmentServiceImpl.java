package org.jala.university.application.service;

import lombok.AllArgsConstructor;
import org.jala.university.application.dto.PurchaseInstallmentDto;
import org.jala.university.application.mapper.PurchaseInstallmentMapper;
import org.jala.university.domain.entity.PurchaseInstallment;
import org.jala.university.domain.repository.PurchaseInstallmentRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
public class PurchaseInstallmentServiceImpl implements PurchaseInstallmentService {

    private final PurchaseInstallmentRepository repository;
    private final PurchaseInstallmentMapper mapper;

    @Override
    public List<PurchaseInstallmentDto> findByInvoiceId(UUID invoiceId) {
        if (invoiceId == null) {
            return List.of();
        }

        try {
            List<PurchaseInstallment> installments = repository.findByInvoiceId(invoiceId);
            return installments.stream()
                    .map(mapper::mapTo)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Em uma aplicação real, aqui entraria um log de erro.
            System.err.println("Error finding installments by invoice ID: " + e.getMessage());
            return List.of();
        }
    }
}
