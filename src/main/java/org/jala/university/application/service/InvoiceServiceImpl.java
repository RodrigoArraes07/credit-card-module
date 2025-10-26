package org.jala.university.application.service;

import org.jala.university.domain.entity.Invoice;
import org.jala.university.domain.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository repository;

    public InvoiceServiceImpl(InvoiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        return repository.save(invoice);
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return repository.findAll();
    }

    @Override
    public Optional<Invoice> getInvoiceById(Long id) {
        return repository.findById(id);
    }
}
