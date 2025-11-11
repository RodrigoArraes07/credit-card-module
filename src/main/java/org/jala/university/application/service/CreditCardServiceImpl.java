package org.jala.university.application.service;

import lombok.AllArgsConstructor;
import org.jala.university.application.dto.CreditCardDto;
import org.jala.university.application.dto.CreditCardRequestDto;
import org.jala.university.application.dto.CreditCardResponseDto;
import org.jala.university.application.mapper.CreditCardMapper;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.User;
import org.jala.university.domain.enums.CreditCardStatus;
import org.jala.university.domain.enums.TargetType;
import org.jala.university.domain.repository.CreditCardRepository;

import java.time.YearMonth;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@AllArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {
    private CreditCardRepository creditCardRepository;
    private CreditCardMapper creditCardMapper;

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
        // TODO: implements
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

    /**
     * @param cardId 
     * @param userId
     * @return
     */
    @Override
    public boolean blockCard(UUID cardId, UUID userId) {
        return false;
    }

    /**
     * @param cardId 
     * @param userId
     * @return
     */
    @Override
    public boolean unblockCard(UUID cardId, UUID userId) {
        return false;
    }

    /**
     * @param cardId 
     * @param userId
     * @return
     */
    @Override
    public boolean cancelCard(UUID cardId, UUID userId) {
        return false;
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
    @Override
    public CreditCardDto blockCardWithMessage(UUID cardId, UUID userId) {
        CreditCard card = creditCardRepository.findById(cardId);
        if (card == null) {
            return buildCreditCardDto(null, "Cartão não encontrado.");
        }
        if (!card.getUser().getId().equals(userId)) {
            return buildCreditCardDto(card, "Usuário não autorizado.");
        }
        if (card.getStatus() == CreditCardStatus.BLOCKED) {
            return buildCreditCardDto(card, "Cartão já está bloqueado.");
        }
        card.setStatus(CreditCardStatus.BLOCKED);
        creditCardRepository.save(card);
        return buildCreditCardDto(card, "Cartão bloqueado com sucesso.");
    }

    @Override
    public CreditCardDto unblockCardWithMessage(UUID cardId, UUID userId) {
        CreditCard card = creditCardRepository.findById(cardId);
        if (card == null) {
            return buildCreditCardDto(null, "Cartão não encontrado.");
        }
        if (!card.getUser().getId().equals(userId)) {
            return buildCreditCardDto(card, "Usuário não autorizado.");
        }
        if (card.getStatus() != CreditCardStatus.BLOCKED) {
            return buildCreditCardDto(card, "O cartão não está bloqueado.");
        }
        card.setStatus(CreditCardStatus.ACTIVE);
        creditCardRepository.save(card);
        return buildCreditCardDto(card, "Cartão desbloqueado com sucesso.");
    }

    @Override
    public CreditCardDto cancelCardWithMessage(UUID cardId, UUID userId) {
        CreditCard card = creditCardRepository.findById(cardId);
        if (card == null) {
            return buildCreditCardDto(null, "Cartão não encontrado.");
        }
        if (!card.getUser().getId().equals(userId)) {
            return buildCreditCardDto(card, "Usuário não autorizado.");
        }
        if (card.getStatus() == CreditCardStatus.CANCELED) {
            return buildCreditCardDto(card, "Cartão já está cancelado.");
        }
        card.setStatus(CreditCardStatus.CANCELED);
        creditCardRepository.save(card);
        return buildCreditCardDto(card, "Cartão cancelado com sucesso.");
    }

    private CreditCardDto buildCreditCardDto(CreditCard card, String message) {
        if (card == null) {
            return CreditCardDto.builder()
                    .message(message)
                    .build();
        }
        CreditCardDto dto = creditCardMapper.toDto(card);
        dto.setMessage(message);
        return dto;
    }



    private CreditCardResponseDto buildErrorResponse(String message) {
        return CreditCardResponseDto.builder()
                .message(message)
                .status(CreditCardStatus.BLOCKED)
                .build();
    }
}
