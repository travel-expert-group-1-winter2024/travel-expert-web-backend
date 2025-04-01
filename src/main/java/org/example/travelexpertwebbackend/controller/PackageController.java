package org.example.travelexpertwebbackend.controller;

import jakarta.annotation.security.PermitAll;
import org.example.travelexpertwebbackend.dto.GenericApiResponse;
import org.example.travelexpertwebbackend.dto.PackageDetailsDTO;
import org.example.travelexpertwebbackend.dto.PackageRequestDTO;
import org.example.travelexpertwebbackend.dto.ProductSupplierDTO;
import org.example.travelexpertwebbackend.dto.auth.LoginResponseDTO;
import org.example.travelexpertwebbackend.service.PackageService;
import org.example.travelexpertwebbackend.entity.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/packages")
public class PackageController {

    @Autowired
    private PackageService packageService;

    //GET Package Details
    @GetMapping
    public ResponseEntity<List<PackageDetailsDTO>> getAllPackages() {
        List<PackageDetailsDTO> packages = packageService.getAllPackages();
        return ResponseEntity.ok(packages);
    }

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

    @GetMapping("/product-supplier")
    public ResponseEntity<List<ProductSupplierDTO>> getAllProductSuppliers() {
        List<ProductSupplierDTO> productSuppliers = packageService.getAllProductSuppliers();
        return ResponseEntity.ok(productSuppliers);
    }

    @GetMapping("/search")
    public ResponseEntity<GenericApiResponse<List<Package>>> searchAndSortPackages(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<Package> packages = packageService.searchAndSort(search, sortBy, order, startDate, endDate);

        return ResponseEntity.ok(new GenericApiResponse<>(packages));  // ✅ Wrap list inside GenericApiResponse
    }
}