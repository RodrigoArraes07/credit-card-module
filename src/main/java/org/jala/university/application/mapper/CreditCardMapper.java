package org.jala.university.application.mapper;

import org.jala.university.application.dto.CreditCardDto;
import org.jala.university.application.dto.InvoiceDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.Invoice;


public class CreditCardMapper implements Mapper<CreditCard, CreditCardDto> {

    private static CreditCardMapper instance;

    private CreditCardMapper() {
    }

    public static CreditCardMapper getInstance() {
        if (instance == null) {
            instance = new CreditCardMapper();
        }
        return instance;
    }

    @Override
    public CreditCardDto mapTo(CreditCard input) {
        return CreditCardDto.builder()
                .credit_card_id(input.getCredit_card_id())
                .cardNumber(input.getCardNumber())
                .maskCardNumber(input.getMaskCardNumber())
                .limitAmount(input.getLimitAmount())
                .targetType(input.getTargetType())
                .cvc(input.getCvc())
                .status(input.getStatus())
                .build();
    }

    @Override
    public CreditCard mapFrom(CreditCardDto input) {
        return CreditCard.builder()
                .credit_card_id(input.getCredit_card_id())
                .cardNumber(input.getCardNumber())
                .maskCardNumber(input.getMaskCardNumber())
                .limitAmount(input.getLimitAmount())
                .targetType(input.getTargetType())
                .cvc(input.getCvc())
                .build();
    }
}
