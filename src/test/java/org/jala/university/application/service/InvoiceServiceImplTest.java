package org.jala.university.application.service;

import org.jala.university.application.dto.InvoiceDetailDto;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("InvoiceService - CRE-007 Tests")
class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private PurchaseInstallmentRepository purchaseInstallmentRepository;

    @Mock
    private InvoiceMapper invoiceMapper;

    @Mock
    private PurchaseInstallmentMapper purchaseInstallmentMapper;

    @Mock
    private CreditCardRepository creditCardRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private UUID creditCardId;
    private UUID invoiceId;
    private Invoice invoice;
    private CreditCard creditCard;

    @BeforeEach
    void setUp() {
        creditCardId = UUID.randomUUID();
        invoiceId = UUID.randomUUID();

        creditCard = new CreditCard();
        creditCard.setLimitAmount(5000.0);
        creditCard.setMaxLimit(10000.0);

        invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setCreditCardId(creditCard);
        invoice.setTotalAmount(1500.0);
        invoice.setIsPaid(false);
        invoice.setClosingDate(LocalDate.of(2025, 11, 18));
        invoice.setDueDate(LocalDate.of(2025, 11, 26));
    }

    @Test
    @DisplayName("Deve processar pagamento de fatura com sucesso")
    void deveProcessarPagamentoComSucesso() {
        when(invoiceRepository.findById(invoiceId)).thenReturn(invoice);

        PaymentResponseDto response = invoiceService.processPayment(invoiceId, PaymentMethod.PIX);

        assertTrue(response.getSuccess());
        assertEquals("Pagamento processado com sucesso!", response.getMessage());
        assertTrue(invoice.getIsPaid());
        assertEquals(PaymentMethod.PIX, invoice.getPaymentMethod());
        assertNotNull(invoice.getPaymentDate());
        verify(invoiceRepository).save(invoice);
    }

    @Test
    @DisplayName("Deve retornar erro quando invoice ID é nulo")
    void deveRetornarErroQuandoInvoiceIdNulo() {
        PaymentResponseDto response = invoiceService.processPayment(null, PaymentMethod.PIX);

        assertFalse(response.getSuccess());
        assertEquals("Dados inválidos. Invoice ID e método de pagamento são obrigatórios.", response.getMessage());
        verify(invoiceRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar erro quando método de pagamento é nulo")
    void deveRetornarErroQuandoMetodoPagamentoNulo() {
        PaymentResponseDto response = invoiceService.processPayment(invoiceId, null);

        assertFalse(response.getSuccess());
        assertEquals("Dados inválidos. Invoice ID e método de pagamento são obrigatórios.", response.getMessage());
        verify(invoiceRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar erro quando fatura não existe")
    void deveRetornarErroQuandoFaturaNaoExiste() {
        when(invoiceRepository.findById(invoiceId)).thenReturn(null);

        PaymentResponseDto response = invoiceService.processPayment(invoiceId, PaymentMethod.PIX);

        assertFalse(response.getSuccess());
        assertEquals("Fatura não encontrada.", response.getMessage());
        verify(invoiceRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar erro quando fatura já está paga")
    void deveRetornarErroQuandoFaturaJaPaga() {
        invoice.setIsPaid(true);
        invoice.setPaymentMethod(PaymentMethod.BOLETO);
        invoice.setPaymentDate(LocalDateTime.now().minusDays(5));

        when(invoiceRepository.findById(invoiceId)).thenReturn(invoice);

        PaymentResponseDto response = invoiceService.processPayment(invoiceId, PaymentMethod.PIX);

        assertFalse(response.getSuccess());
        assertEquals("Esta fatura já foi paga.", response.getMessage());
        verify(invoiceRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve liberar limite do cartão após pagamento")
    void deveLiberarLimiteDoCartaoAposPagamento() {
        invoice.setCreditCardId(creditCard);
        when(invoiceRepository.findById(invoiceId)).thenReturn(invoice);

        invoiceService.processPayment(invoiceId, PaymentMethod.PIX);

        double expectedLimit = 5000.0 + 1500.0;
        assertEquals(expectedLimit, creditCard.getLimitAmount());
        verify(creditCardRepository).save(creditCard);
    }

    @Test
    @DisplayName("Deve respeitar limite máximo do cartão ao liberar")
    void deveRespeitarLimiteMaximoDoCartao() {
        creditCard.setLimitAmount(9500.0);
        invoice.setTotalAmount(1000.0);
        invoice.setCreditCardId(creditCard);

        when(invoiceRepository.findById(invoiceId)).thenReturn(invoice);

        invoiceService.processPayment(invoiceId, PaymentMethod.PIX);

        assertEquals(10000.0, creditCard.getLimitAmount());
        verify(creditCardRepository).save(creditCard);
    }

    @Test
    @DisplayName("Deve retornar todas as faturas do cartão ordenadas")
    void deveRetornarTodasFaturasOrdenadas() {
        List<Invoice> invoices = criarListaFaturas();
        List<PurchaseInstallment> installments = new ArrayList<>();
        InvoiceDetailDto detailDto = criarInvoiceDetailDto();

        when(invoiceRepository.findByCreditCardId(creditCardId)).thenReturn(invoices);
        when(purchaseInstallmentRepository.findByInvoiceId(any())).thenReturn(installments);
        when(invoiceMapper.toDetailDto(any(), any())).thenReturn(detailDto);

        List<InvoiceDetailDto> result = invoiceService.getAllInvoicesByCreditCard(creditCardId);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(invoiceRepository).findByCreditCardId(creditCardId);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando cartão ID é nulo")
    void deveRetornarListaVaziaQuandoCartaoIdNulo() {
        List<InvoiceDetailDto> result = invoiceService.getAllInvoicesByCreditCard(null);

        assertTrue(result.isEmpty());
        verify(invoiceRepository, never()).findByCreditCardId(any());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há faturas")
    void deveRetornarListaVaziaQuandoNaoHaFaturas() {
        when(invoiceRepository.findByCreditCardId(creditCardId)).thenReturn(new ArrayList<>());

        List<InvoiceDetailDto> result = invoiceService.getAllInvoicesByCreditCard(creditCardId);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando ocorre exceção")
    void deveRetornarListaVaziaQuandoOcorreExcecao() {
        when(invoiceRepository.findByCreditCardId(creditCardId))
                .thenThrow(new RuntimeException("Database error"));

        List<InvoiceDetailDto> result = invoiceService.getAllInvoicesByCreditCard(creditCardId);

        assertTrue(result.isEmpty());
    }

    private List<Invoice> criarListaFaturas() {
        Invoice i1 = new Invoice();
        i1.setId(UUID.randomUUID());
        i1.setClosingDate(LocalDate.of(2025, 10, 18));

        Invoice i2 = new Invoice();
        i2.setId(UUID.randomUUID());
        i2.setClosingDate(LocalDate.of(2025, 11, 18));

        Invoice i3 = new Invoice();
        i3.setId(UUID.randomUUID());
        i3.setClosingDate(LocalDate.of(2025, 12, 18));

        return List.of(i2, i1, i3);
    }

    private InvoiceDetailDto criarInvoiceDetailDto() {
        return InvoiceDetailDto.builder()
                .id(UUID.randomUUID())
                .closingDate(LocalDate.of(2025, 11, 18))
                .dueDate(LocalDate.of(2025, 11, 26))
                .totalAmount(1500.0)
                .isPaid(false)
                .installments(new ArrayList<>())
                .build();
    }
}
