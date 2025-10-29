package org.jala.university.application.service;


import lombok.AllArgsConstructor;
import org.jala.university.application.dto.CreditCardCreateDto;
import org.jala.university.application.dto.CreditCardDto;
import org.jala.university.application.mapper.CreditCardMapper;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.repository.CreditCardRepository;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {
    private CreditCardRepository creditCardRepository;
    private CreditCardMapper creditCardMapper;

    @Override
    public List<CreditCardDto> findAll(){
        return List.of();
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
        //TODO: implements
    }

}
