package org.jala.university.application.service;

import lombok.AllArgsConstructor;
import org.jala.university.application.dto.CreditCardDto;
import org.jala.university.application.dto.CreditCardRequestDto;
import org.jala.university.application.dto.CreditCardResponseDto;
import org.jala.university.application.dto.CreditCardUpdateDto;
import org.jala.university.application.dto.CreditCardUpdateResponseDto;
import org.jala.university.application.mapper.CreditCardMapper;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.Invoice;
import org.jala.university.domain.entity.User;
import org.jala.university.domain.enums.CreditCardStatus;
import org.jala.university.domain.enums.TargetType;
import org.jala.university.domain.repository.CreditCardRepository;
import org.jala.university.domain.repository.InvoiceRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@AllArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {
    private CreditCardRepository creditCardRepository;
    private CreditCardMapper creditCardMapper;
    private InvoiceRepository invoiceRepository;

    @Override
    public List<CreditCardDto> findAll() {
        return List.of();
    }

    @Override
    public List<CreditCardDto> findByUserId(UUID userId) {
        List<CreditCard> cards = creditCardRepository.findByUserId(userId);
        return cards.stream()
                .map(creditCardMapper::mapTo)
                .toList();
    }

    @Override
    public CreditCardDto findById(UUID id) {
        return null;
    }

    @Override
    public CreditCardDto create(CreditCard creditCard) {
        return null;
    }

    @Override
    public CreditCardDto update(UUID id, CreditCard creditCard) {
        return null;
    }

    @Override
    public void deleteById(UUID id) {
    }

    @Override
    public CreditCardResponseDto requestCreditCard(CreditCardRequestDto request, User user) {
        try {
            if (!isValidRequest(request)) {
                return buildErrorResponse("Dados inválidos. Verifique as informações.");
            }

            if (user == null) {
                return buildErrorResponse("Usuário não encontrado.");
            }

            Double approvedLimit = calculatePreApprovedLimit(request.getMonthlyIncome());

            String cardNumber = generateCardNumber();
            String maskedNumber = maskCardNumber(cardNumber);
            int cvv = generateCVV();
            YearMonth expiration = YearMonth.now().plusYears(5);
            TargetType targetType = TargetType.Visa;

            CreditCard creditCard = CreditCard.builder()
                    .cardNumber(cardNumber)
                    .maskCardNumber(maskedNumber)
                    .holderName(request.getHolderName())
                    .limitAmount(approvedLimit)
                    .maxLimit(approvedLimit)
                    .targetType(targetType)
                    .cardType(request.getCardType())
                    .cvc(cvv)
                    .expiration(expiration)
                    .passwordHash(request.getPassword())
                    .status(CreditCardStatus.ACTIVE)
                    .user(user)
                    .build();

            CreditCard saved = creditCardRepository.save(creditCard);

            return CreditCardResponseDto.builder()
                    .creditCardId(saved.getCredit_card_id())
                    .maskCardNumber(maskedNumber)
                    .holderName(request.getHolderName())
                    .approvedLimit(approvedLimit)
                    .targetType(targetType)
                    .expiration(expiration)
                    .status(CreditCardStatus.ACTIVE)
                    .message("Cartão criado com sucesso!")
                    .build();

        } catch (Exception e) {
            return buildErrorResponse("Erro ao processar solicitação: " + e.getMessage());
        }
    }

    @Override
    public Double calculatePreApprovedLimit(Double monthlyIncome) {
        if (monthlyIncome == null || monthlyIncome < 0) {
            return 0.0;
        }
        if (monthlyIncome < 1000) {
            return 500.0;
        }
        return monthlyIncome * 2.5;
    }

    private boolean isValidRequest(CreditCardRequestDto request) {
        return request.getHolderName() != null && !request.getHolderName().isBlank()
                && request.getCpf() != null && !request.getCpf().isBlank()
                && request.getMonthlyIncome() != null && request.getMonthlyIncome() > 0
                && request.getEmail() != null && !request.getEmail().isBlank()
                && request.getPassword() != null && request.getPassword().length() == 4;
    }

    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder number = new StringBuilder("4");
        for (int i = 0; i < 15; i++) {
            number.append(random.nextInt(10));
        }
        return number.toString();
    }

    private String maskCardNumber(String cardNumber) {
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

    private int generateCVV() {
        Random random = new Random();
        return random.nextInt(900) + 100;
    }

    private CreditCardResponseDto buildErrorResponse(String message) {
        return CreditCardResponseDto.builder()
                .message(message)
                .status(CreditCardStatus.BLOCKED)
                .build();
    }

    @Override
    public CreditCardUpdateResponseDto updateCreditCardSettings(CreditCardUpdateDto updateDto) {
        try {
            CreditCard creditCard = creditCardRepository.findByCardNumber(updateDto.getCardNumber());
            if (creditCard == null) {
                return buildUpdateErrorResponse("Cartão não encontrado.");
            }

            if (updateDto.getPassword() == null || !creditCard.getPasswordHash().equals(updateDto.getPassword())) {
                return buildUpdateErrorResponse("Senha incorreta.");
            }

            boolean limitUpdated = false;
            boolean dueDateUpdated = false;
            int affectedInvoices = 0;

            if (updateDto.getNewLimitAmount() != null) {
                if (!isValidLimit(updateDto.getNewLimitAmount(), creditCard.getMaxLimit())) {
                    return buildUpdateErrorResponse("Limite inválido. Deve ser >= 50 e <= " + creditCard.getMaxLimit());
                }
                creditCard.setLimitAmount(updateDto.getNewLimitAmount());
                limitUpdated = true;
            }

            if (updateDto.getNewDueDay() != null) {
                if (!isValidDueDay(updateDto.getNewDueDay())) {
                    return buildUpdateErrorResponse("Dia de vencimento inválido. Use múltiplos de 5 (5, 10, 15, 20, 25, 30).");
                }

                List<Invoice> futureInvoices = invoiceRepository.findFutureInvoicesByCreditCard(creditCard, LocalDate.now());
                for (Invoice invoice : futureInvoices) {
                    LocalDate newDueDate = invoice.getDueDate().withDayOfMonth(updateDto.getNewDueDay());
                    invoice.setDueDate(newDueDate);
                    invoiceRepository.save(invoice);
                    affectedInvoices++;
                }
                dueDateUpdated = true;
            }

            if (limitUpdated) {
                creditCardRepository.save(creditCard);
            }

            return CreditCardUpdateResponseDto.builder()
                    .maskCardNumber(creditCard.getMaskCardNumber())
                    .updatedLimit(limitUpdated ? creditCard.getLimitAmount() : null)
                    .updatedDueDay(dueDateUpdated ? updateDto.getNewDueDay() : null)
                    .affectedInvoices(affectedInvoices)
                    .message("Cartão atualizado com sucesso!")
                    .success(true)
                    .build();

        } catch (Exception e) {
            return buildUpdateErrorResponse("Erro ao atualizar cartão: " + e.getMessage());
        }
    }

    private boolean isValidLimit(Double newLimit, Double maxLimit) {
        return newLimit != null && newLimit >= 50 && newLimit <= maxLimit;
    }

    private boolean isValidDueDay(Integer dueDay) {
        return dueDay != null && dueDay % 5 == 0 && dueDay >= 5 && dueDay <= 30;
    }

    private CreditCardUpdateResponseDto buildUpdateErrorResponse(String message) {
        return CreditCardUpdateResponseDto.builder()
                .message(message)
                .success(false)
                .build();
    }
}
