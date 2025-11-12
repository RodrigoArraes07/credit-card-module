package org.jala.university.application.mapper;

import org.jala.university.application.dto.CreditCardDto;
import org.jala.university.application.dto.InvoiceDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.Invoice;

public class CreditCardMapper implements Mapper<CreditCard, CreditCardDto> {
    /**
     * Converte uma entidade CreditCard em um CreditCardCreateDto,
     * mascarando informações sensíveis como o número do cartão de crédito.
     *
     * @param input a entidade CreditCard a ser convertida
     * @return CreditCardDto com dados filtrados e prontos para exposição ao usuário
     */
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
                .holderName(input.getHolderName())
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
