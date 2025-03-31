package org.example.travelexpertwebbackend.controller;

import org.example.travelexpertwebbackend.entity.SupplierContact;
import org.example.travelexpertwebbackend.service.SupplierContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliercontacts")
@CrossOrigin(origins = "*")
public class SupplierContactController {

    @Autowired
    private SupplierContactService service;

    @GetMapping
    public List<SupplierContact> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public SupplierContact getById(@PathVariable int id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public SupplierContact update(@PathVariable int id, @RequestBody SupplierContact updated) {
        updated.setId(id);
        return service.save(updated);
    }
}
