package org.jala.university.application.service;

import lombok.AllArgsConstructor;
import org.jala.university.application.dto.InvoiceCreateDTO;
import org.jala.university.application.dto.InvoiceDetailDto;
import org.jala.university.application.dto.InvoiceDto;
import org.jala.university.application.dto.InvoiceTimelineDto;
import org.jala.university.application.dto.PurchaseInstallmentDto;
import org.jala.university.application.mapper.InvoiceMapper;
import org.jala.university.application.mapper.PurchaseInstallmentMapper;
import org.jala.university.domain.entity.Invoice;
import org.jala.university.domain.entity.PurchaseInstallment;
import org.jala.university.domain.repository.InvoiceRepository;
import org.jala.university.domain.repository.PurchaseInstallmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PurchaseInstallmentRepository purchaseInstallmentRepository;
    private final InvoiceMapper invoiceMapper;
    private final PurchaseInstallmentMapper purchaseInstallmentMapper;

    @Override
    public List<InvoiceDto> findAll() {
        try {
            List<Invoice> invoices = invoiceRepository.findAll();
            return invoices.stream()
                    .map(invoiceMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public InvoiceDto findById(UUID id) {
        try {
            Invoice invoice = invoiceRepository.findById(id);
            return invoiceMapper.toDto(invoice);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Invoice create(InvoiceCreateDTO invoiceDto) {
        return null;
    }

    @Override
    public InvoiceDto update(UUID id, InvoiceDto invoiceDto) {
        return null;
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public Optional<InvoiceTimelineDto> getInvoiceTimeline(UUID creditCardId) {
        if (creditCardId == null) {
            return Optional.empty();
        }

        try {
            List<Invoice> allInvoices = invoiceRepository.findByCreditCardId(creditCardId);

            if (allInvoices == null || allInvoices.isEmpty()) {
                return Optional.empty();
            }

            Invoice currentInvoice = allInvoices.stream()
                    .filter(i -> !i.getIsPaid())
                    .min((i1, i2) -> i1.getClosingDate().compareTo(i2.getClosingDate()))
                    .orElse(null);

            if (currentInvoice == null) {
                return Optional.empty();
            }

            Invoice previousInvoice = allInvoices.stream()
                    .filter(i -> i.getClosingDate().isBefore(currentInvoice.getClosingDate()))
                    .max((i1, i2) -> i1.getClosingDate().compareTo(i2.getClosingDate()))
                    .orElse(null);

            Invoice nextInvoice = allInvoices.stream()
                    .filter(i -> i.getClosingDate().isAfter(currentInvoice.getClosingDate()))
                    .min((i1, i2) -> i1.getClosingDate().compareTo(i2.getClosingDate()))
                    .orElse(null);

            List<PurchaseInstallment> currentInstallments = purchaseInstallmentRepository.findByInvoiceId(currentInvoice.getId());
            List<PurchaseInstallment> previousInstallments = previousInvoice != null ?
                    purchaseInstallmentRepository.findByInvoiceId(previousInvoice.getId()) : List.of();
            List<PurchaseInstallment> nextInstallments = nextInvoice != null ?
                    purchaseInstallmentRepository.findByInvoiceId(nextInvoice.getId()) : List.of();

            InvoiceDetailDto currentDetailDto = invoiceMapper.toDetailDto(
                    currentInvoice,
                    currentInstallments.stream().map(purchaseInstallmentMapper::mapTo).toList()
            );

            InvoiceDetailDto previousDetailDto = previousInvoice != null ?
                    invoiceMapper.toDetailDto(
                            previousInvoice,
                            previousInstallments.stream().map(purchaseInstallmentMapper::mapTo).toList()
                    ) : null;

            InvoiceDetailDto nextDetailDto = nextInvoice != null ?
                    invoiceMapper.toDetailDto(
                            nextInvoice,
                            nextInstallments.stream().map(purchaseInstallmentMapper::mapTo).toList()
                    ) : null;

            InvoiceTimelineDto timeline = InvoiceTimelineDto.builder()
                    .previousInvoice(previousDetailDto)
                    .currentInvoice(currentDetailDto)
                    .nextInvoice(nextDetailDto)
                    .year(currentInvoice.getClosingDate().getYear())
                    .build();

            return Optional.of(timeline);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<InvoiceDetailDto> getInvoiceDetail(UUID invoiceId) {
        if (invoiceId == null) {
            return Optional.empty();
        }

        try {
            Invoice invoice = invoiceRepository.findById(invoiceId);
            if (invoice == null) {
                return Optional.empty();
            }

            List<PurchaseInstallment> installments = purchaseInstallmentRepository.findByInvoiceId(invoiceId);
            List<PurchaseInstallmentDto> installmentDtos = installments.stream()
                    .map(purchaseInstallmentMapper::mapTo)
                    .toList();

            InvoiceDetailDto detailDto = invoiceMapper.toDetailDto(invoice, installmentDtos);
            return Optional.of(detailDto);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<InvoiceDto> getInvoicesByYear(UUID creditCardId, int year) {
        if (creditCardId == null) {
            return List.of();
        }

        try {
            List<Invoice> invoices = invoiceRepository.findByYear(creditCardId, year);
            return invoices.stream()
                    .map(invoiceMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }
}
