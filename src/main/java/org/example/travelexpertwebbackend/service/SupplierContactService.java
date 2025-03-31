package org.example.travelexpertwebbackend.service;

import org.example.travelexpertwebbackend.entity.SupplierContact;
import org.example.travelexpertwebbackend.repository.SupplierContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierContactService {

    @Autowired
    private SupplierContactRepository repository;

    public List<SupplierContact> getAll() {
        return repository.findAll();
    }

    public SupplierContact getById(int id) {
        return repository.findById(id).orElse(null);
    }

    public SupplierContact save(SupplierContact contact) {
        return repository.save(contact);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }
}
