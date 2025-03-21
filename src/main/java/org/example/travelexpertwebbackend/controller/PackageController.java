package org.example.travelexpertwebbackend.controller;

import org.example.travelexpertwebbackend.dto.PackageDetailsDTO;
import org.example.travelexpertwebbackend.dto.PackageRequestDTO;
import org.example.travelexpertwebbackend.service.PackageService;
import org.example.travelexpertwebbackend.entity.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/packages")
public class PackageController {

    @Autowired
    private PackageService packageService;

    //GET Package Details
    @GetMapping("/{id}/details")
    public ResponseEntity<PackageDetailsDTO> getPackageDetails(@PathVariable Integer id) {
        PackageDetailsDTO dto = packageService.getPackageDetails(id);
        return ResponseEntity.ok(dto);
    }

    //POST create new package
    @PostMapping
    public ResponseEntity<Package> createPackage(@RequestBody PackageRequestDTO request) {
        Package createdPackage = packageService.createPackage(request);
        return ResponseEntity.ok(createdPackage);
    }

    //PUT updating package
    @PutMapping("/{id}")
    public ResponseEntity<Package> updatePackage(@PathVariable Integer id, @RequestBody PackageRequestDTO request) {
        Package updatedPackage = packageService.updatePackage(id, request);
        return ResponseEntity.ok(updatedPackage);
    }

    //DELETE deleting package
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable Integer id) {
        packageService.deletePackage(id);
        return ResponseEntity.noContent().build();
    }
}