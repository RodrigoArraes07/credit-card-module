package org.jala.university.application.service;

import lombok.AllArgsConstructor;
import org.jala.university.application.dto.CreditCardDto;
import org.jala.university.application.dto.CreditCardRequestDto;
import org.jala.university.application.dto.CreditCardResponseDto;
import org.jala.university.application.dto.CreditCardStatusUpdateRequestDto;
import org.jala.university.application.dto.CreditCardStatusUpdateResponseDto;
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
import org.jala.university.infrastructure.util.PasswordHashUtil;

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
    public CreditCardDto findByCardNumber(String cardNumber) {
        CreditCard card = creditCardRepository.findByCardNumber(cardNumber);
        return card != null ? creditCardMapper.mapTo(card) : null;
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

            if (updateDto.getNewLimitAmount() == null && updateDto.getNewDueDay() == null) {
                return buildUpdateErrorResponse("Informe ao menos limite ou dia de vencimento para alterar.");
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
                int requestedDay = updateDto.getNewDueDay();
                for (Invoice invoice : futureInvoices) {
                    int lastDay = invoice.getDueDate().lengthOfMonth();
                    int effectiveDay = Math.min(requestedDay, lastDay);
                    LocalDate newDueDate = invoice.getDueDate().withDayOfMonth(effectiveDay);
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

    @Override
    public Double calculateAvailableLimit(UUID creditCardId) {
        if (creditCardId == null) {
            return 0.0;
        }

        try {
            CreditCard card = creditCardRepository.findById(creditCardId);
            if (card == null || card.getMaxLimit() == null) {
                return 0.0;
            }

            Double maxLimit = card.getMaxLimit();
            List<Invoice> unpaidInvoices = invoiceRepository.findByCreditCardId(creditCardId);

            if (unpaidInvoices == null || unpaidInvoices.isEmpty()) {
                return maxLimit;
            }

            Double totalUnpaid = unpaidInvoices.stream()
                    .filter(invoice -> invoice.getIsPaid() == null || !invoice.getIsPaid())
                    .map(Invoice::getTotalAmount)
                    .reduce(0.0, Double::sum);

            Double availableLimit = maxLimit - totalUnpaid;
            return Math.max(availableLimit, 0.0);

        } catch (Exception e) {
            System.err.println("Error calculating available limit: " + e.getMessage());
            return 0.0;
        }
    }

    @Override
    public CreditCardStatusUpdateResponseDto blockCard(CreditCardStatusUpdateRequestDto requestDto) {
        return changeCardStatus(requestDto, CreditCardStatus.BLOCKED,
                "Cartão já está bloqueado.",
                "Cartão bloqueado temporariamente com sucesso.");
    }

    @Override
    public CreditCardStatusUpdateResponseDto unblockCard(CreditCardStatusUpdateRequestDto requestDto) {
        return changeCardStatus(requestDto, CreditCardStatus.ACTIVE,
                "Cartão já está desbloqueado.",
                "Cartão desbloqueado com sucesso.");
    }

    private CreditCardStatusUpdateResponseDto changeCardStatus(
            CreditCardStatusUpdateRequestDto requestDto,
            CreditCardStatus desiredStatus,
            String alreadyMessage,
            String successMessage) {
        if (requestDto == null
                || requestDto.getUserId() == null
                || requestDto.getUserPassword() == null
                || requestDto.getCardNumber() == null) {
            return buildStatusErrorResponse(null, "Dados inválidos para alterar status do cartão.");
        }

        try {
            CreditCard creditCard = creditCardRepository.findByCardNumber(requestDto.getCardNumber());
            if (creditCard == null) {
                return buildStatusErrorResponse(null, "Cartão não encontrado.");
            }

            User user = creditCard.getUser();
            if (user == null || user.getId() == null || !user.getId().equals(requestDto.getUserId())) {
                return buildStatusErrorResponse(creditCard, "Usuário não autorizado para este cartão.");
            }

            if (!creditCard.getPasswordHash().equals(requestDto.getUserPassword())) {
                return buildStatusErrorResponse(creditCard, "Senha do cartão incorreta.");
            }

            if (creditCard.getStatus() == desiredStatus) {
                return CreditCardStatusUpdateResponseDto.builder()
                        .maskCardNumber(creditCard.getMaskCardNumber())
                        .status(creditCard.getStatus())
                        .message(alreadyMessage)
                        .success(true)
                        .build();
            }

            if (!canChangeStatus(creditCard.getStatus(), desiredStatus)) {
                return buildStatusErrorResponse(creditCard, "Status do cartão não permite esta operação.");
            }

            creditCard.setStatus(desiredStatus);
            creditCardRepository.save(creditCard);

            return CreditCardStatusUpdateResponseDto.builder()
                    .maskCardNumber(creditCard.getMaskCardNumber())
                    .status(creditCard.getStatus())
                    .message(successMessage)
                    .success(true)
                    .build();

        } catch (Exception e) {
            return buildStatusErrorResponse(null, "Erro ao alterar status do cartão: " + e.getMessage());
        }
    }

    private boolean canChangeStatus(CreditCardStatus currentStatus, CreditCardStatus desiredStatus) {
        if (currentStatus == null || desiredStatus == null) {
            return false;
        }

        if (desiredStatus == CreditCardStatus.BLOCKED) {
            return currentStatus == CreditCardStatus.ACTIVE;
        }

        if (desiredStatus == CreditCardStatus.ACTIVE) {
            return currentStatus == CreditCardStatus.BLOCKED;
        }

        return false;
    }

    private CreditCardStatusUpdateResponseDto buildStatusErrorResponse(CreditCard creditCard, String message) {
        String mask = creditCard != null ? creditCard.getMaskCardNumber() : null;
        return CreditCardStatusUpdateResponseDto.builder()
                .maskCardNumber(mask)
                .status(creditCard != null ? creditCard.getStatus() : null)
                .message(message)
                .success(false)
                .build();
    }
}
