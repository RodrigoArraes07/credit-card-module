package org.jala.university.application.mapper;

import org.jala.university.application.dto.CreditCardDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.CreditCardStatus;

public class CreditCardMapper implements Mapper<CreditCard, CreditCardDto> {
    private static final CreditCardMapper INSTANCE = new CreditCardMapper();

    private CreditCardMapper() {
    }

    public static CreditCardMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public final CreditCardDto mapTo(CreditCard creditCard) {
        if (creditCard == null) return null;

        return CreditCardDto.builder()
                .credit_card_id(creditCard.getCredit_card_id())
                .cardNumber(creditCard.getCardNumber())
                .holderName(creditCard.getHolderName())
                .expiration(creditCard.getExpiration())
                .limitAmount(creditCard.getLimitAmount())
                .balance(creditCard.getBalance())
                .cvc(creditCard.getCvc())
                .status(creditCard.getStatus() != null ? CreditCardStatus.valueOf(creditCard.getStatus().name()) : null)
                .build();
    }

    @Override
    public final CreditCard mapFrom(CreditCardDto dto) {
        if (dto == null) return null;

        CreditCard.CreditCardBuilder builder = CreditCard.builder()
                .credit_card_id(dto.getCredit_card_id())
                .cardNumber(dto.getCardNumber())
                .holderName(dto.getHolderName())
                .expiration(dto.getExpiration())
                .limitAmount(dto.getLimitAmount())
                .balance(dto.getBalance())
                .cvc(dto.getCvc());

        if (dto.getStatus() != null) {
            try {
                builder.status(CreditCardStatus.valueOf(dto.getStatus().name()));
            } catch (IllegalArgumentException e) {
                builder.status(null);
            }
        }

        return builder.build();
    }

    public final CreditCard mapFromCreateDto(CreditCardDto createDto) {
        if (createDto == null) return null;

        CreditCard.CreditCardBuilder builder = CreditCard.builder()
                .cardNumber(createDto.getCardNumber())
                .holderName(createDto.getHolderName())
                .expiration(createDto.getExpiration())
                .limitAmount(createDto.getLimitAmount())
                .balance(createDto.getBalance())
                .cvc(createDto.getCvc());

        if (createDto.getStatus() != null) {
            try {
                builder.status(CreditCardStatus.valueOf(createDto.getStatus().name()));
            } catch (IllegalArgumentException e) {
                builder.status(null);
            }
        }

        return builder.build();
    }
}
