package org.jala.university.application.service;

import lombok.AllArgsConstructor;
import org.jala.university.application.dto.InvoiceCreateDTO;
import org.jala.university.application.dto.InvoiceDto;
import org.jala.university.domain.entity.Invoice;
import org.jala.university.domain.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    @Override
    public List<InvoiceDto> findAll() {
        return List.of();
    }

    @Override
    public InvoiceDto findById(UUID id) {
        return null;
    }

    @Override
    public InvoiceCreateDTO create(InvoiceCreateDTO invoiceDto) {
        return null;
    }

    @Override
    public InvoiceDto update(UUID id, InvoiceDto invoiceDto) {
        return null;
    }

    @Override
    public void deleteById(UUID id) {

    }
}
