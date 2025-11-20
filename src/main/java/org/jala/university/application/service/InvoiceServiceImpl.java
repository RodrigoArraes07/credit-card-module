package org.jala.university.application.service;

import lombok.AllArgsConstructor;
import org.jala.university.application.dto.InvoiceCreateDTO;
import org.jala.university.application.dto.InvoiceDetailDto;
import org.jala.university.application.dto.InvoiceDto;
import org.jala.university.application.dto.InvoiceTimelineDto;
import org.jala.university.application.dto.PaymentResponseDto;
import org.jala.university.application.dto.PurchaseInstallmentDto;
import org.jala.university.application.mapper.InvoiceMapper;
import org.jala.university.application.mapper.PurchaseInstallmentMapper;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.Invoice;
import org.jala.university.domain.entity.PurchaseInstallment;
import org.jala.university.domain.enums.PaymentMethod;
import org.jala.university.domain.repository.CreditCardRepository;
import org.jala.university.domain.repository.InvoiceRepository;
import org.jala.university.domain.repository.PurchaseInstallmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private static final double LATE_FEE_PERCENTAGE = 0.02; // 2%
    private static final double DAILY_INTEREST_RATE = 0.001; // 0.1%

    private final InvoiceRepository invoiceRepository;
    private final PurchaseInstallmentRepository purchaseInstallmentRepository;
    private final InvoiceMapper invoiceMapper;
    private final PurchaseInstallmentMapper purchaseInstallmentMapper;
    private final CreditCardRepository creditCardRepository;

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

            LocalDate today = LocalDate.now();
            int currentMonth = today.getMonthValue();
            int currentYear = today.getYear();

            Invoice currentInvoice = allInvoices.stream()
                    .filter(i -> i.getClosingDate().getMonthValue() == currentMonth
                            && i.getClosingDate().getYear() == currentYear)
                    .findFirst()
                    .orElse(
                            allInvoices.stream()
                                    .max((i1, i2) -> i1.getClosingDate().compareTo(i2.getClosingDate()))
                                    .orElse(null)
                    );

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

            applyLateCharges(invoice); //aplicar as mudanças de valor devido a atraso
            invoiceRepository.save(invoice);

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

    @Override
    public List<InvoiceDetailDto> getAllInvoicesByCreditCard(UUID creditCardId) {
        if (creditCardId == null) {
            return List.of();
        }

        try {
            List<Invoice> invoices = invoiceRepository.findByCreditCardId(creditCardId);

            if (invoices == null || invoices.isEmpty()) {
                return List.of();
            }

            return invoices.stream()
                    .sorted((i1, i2) -> i1.getClosingDate().compareTo(i2.getClosingDate()))
                    .map(invoice -> {
                        applyLateCharges(invoice); //aplicar as mudanças de valor devido a atraso
                        invoiceRepository.save(invoice);

                        List<PurchaseInstallment> installments = purchaseInstallmentRepository.findByInvoiceId(invoice.getId());
                        List<PurchaseInstallmentDto> installmentDtos = installments.stream()
                                .map(purchaseInstallmentMapper::mapTo)
                                .toList();
                        return invoiceMapper.toDetailDto(invoice, installmentDtos);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public PaymentResponseDto processPayment(UUID invoiceId, PaymentMethod method) {
        if (invoiceId == null || method == null) {
            return PaymentResponseDto.builder()
                    .success(false)
                    .message("Dados inválidos. Invoice ID e método de pagamento são obrigatórios.")
                    .build();
        }

        try {
            Invoice invoice = invoiceRepository.findById(invoiceId);

            if (invoice == null) {
                return PaymentResponseDto.builder()
                        .success(false)
                        .message("Fatura não encontrada.")
                        .invoiceId(invoiceId)
                        .build();
            }

            if (invoice.getIsPaid()) {
                return PaymentResponseDto.builder()
                        .success(false)
                        .message("Esta fatura já foi paga.")
                        .invoiceId(invoiceId)
                        .paymentMethod(invoice.getPaymentMethod())
                        .paymentDate(invoice.getPaymentDate())
                        .build();
            }

            LocalDateTime now = LocalDateTime.now();
            invoice.setPaymentMethod(method);
            invoice.setPaymentDate(now);
            invoice.setIsPaid(true);

            invoiceRepository.save(invoice);

            CreditCard creditCard = invoice.getCreditCardId();
            if (creditCard != null) {
                Double currentLimit = creditCard.getLimitAmount() != null ? creditCard.getLimitAmount() : 0.0;
                Double newLimit = currentLimit + invoice.getTotalAmount();

                if (creditCard.getMaxLimit() != null && newLimit > creditCard.getMaxLimit()) {
                    newLimit = creditCard.getMaxLimit();
                }

                creditCard.setLimitAmount(newLimit);
                creditCardRepository.save(creditCard);
            }

            return PaymentResponseDto.builder()
                    .success(true)
                    .message("Pagamento processado com sucesso!")
                    .invoiceId(invoice.getId())
                    .paymentMethod(method)
                    .paymentDate(now)
                    .build();

        } catch (Exception e) {
            return PaymentResponseDto.builder()
                    .success(false)
                    .message("Erro ao processar pagamento: " + e.getMessage())
                    .invoiceId(invoiceId)
                    .build();
        }
    }

    /*
    * CRE-008: Implementar método para aplicar multas e juros em faturas vencidas
    * Requisitos:
    * 1. O método deve verificar se a fatura está vencida (data de vencimento anterior à data atual).
    * 2. Se a fatura estiver vencida, deve calcular uma multa de 2% sobre o valor total da fatura.
    * 3. Deve calcular juros de 0,1% ao dia sobre o valor total da fatura para cada dia de atraso.
    * 4. O método deve atualizar os campos de multa e juros na entidade Fatura.
    * 5. O método deve ser chamado sempre que uma fatura for recuperada para exibição ou processamento de pagamento.
    * 6. Garantir que o método seja testado através de testes unitários.
    */

    @Override
    public void applyLateCharges(Invoice invoice) {
        if (invoice == null || invoice.getIsPaid() || invoice.getDueDate().isAfter(LocalDate.now())) {
            // Nada a fazer se a fatura for nula, já paga ou não estiver vencida
            return;
        }

        long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(invoice.getDueDate(), LocalDate.now());
        
        // esse metodo calcula a diferença em dias entre duas datas, se a data de vencimento for maior que a data atual, o valor será negativo
        if (overdueDays <= 0) {
            return;
        }

        // Calcula a multa uma única vez de acordo com o valor total da fatura.
        double lateFee = invoice.getTotalAmount() * LATE_FEE_PERCENTAGE;
        invoice.setLateFee(lateFee);

        // Calcula os juros com base nos dias de atraso acumulados.
        double interest = invoice.getTotalAmount() * DAILY_INTEREST_RATE * overdueDays;
        invoice.setInterestCharges(interest);
    }
    //essa parte foi feita apartir dos testes criados no InvoiceServiceImplTest.java da CRE-008
}
