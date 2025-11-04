package org.jala.university.application.service;

import lombok.AllArgsConstructor;
import org.jala.university.application.dto.InvoiceCreateDTO;
import org.jala.university.application.dto.InvoiceDto;
import org.jala.university.application.mapper.InvoiceMapper;
import org.jala.university.domain.entity.Invoice;
import org.jala.university.domain.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private InvoiceRepository invoiceRepository;
    private InvoiceMapper invoiceMapper;

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
}
