package org.example.travelexpertwebbackend.service;

import jakarta.transaction.Transactional;
import org.example.travelexpertwebbackend.dto.PackageDetailsDTO;
import org.example.travelexpertwebbackend.dto.PackageRequestDTO;
import org.example.travelexpertwebbackend.dto.ProductSupplierDTO;
import org.example.travelexpertwebbackend.entity.Package;
import org.example.travelexpertwebbackend.entity.ProductsSupplier;
import org.example.travelexpertwebbackend.repository.PackageRepository;
import org.example.travelexpertwebbackend.repository.ProductsSupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private ProductsSupplierRepository productsSupplierRepository;

    public PackageDetailsDTO getPackageDetails(Integer id) {
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        PackageDetailsDTO dto = new PackageDetailsDTO();
        dto.setPackageid(pkg.getId());
        dto.setPkgname(pkg.getPkgname());
        dto.setPkgstartdate(pkg.getPkgstartdate());
        dto.setPkgenddate(pkg.getPkgenddate());
        dto.setPkgdesc(pkg.getPkgdesc());
        dto.setPkgbaseprice(pkg.getPkgbaseprice());
        dto.setPkgagencycommission(pkg.getPkgagencycommission());

        Set<ProductSupplierDTO> productSupplierDTOs = pkg.getProductsSuppliers().stream()
                .map(this::mapToProductSupplierDTO)
                .collect(Collectors.toSet());

        dto.setProductsSuppliers(productSupplierDTOs);
        return dto;
    }

    private ProductSupplierDTO mapToProductSupplierDTO(ProductsSupplier ps) {
        ProductSupplierDTO dto = new ProductSupplierDTO();

        // Check for null productid
        if (ps.getProductid() != null) {
            dto.setProductId(ps.getProductid().getId());
            dto.setProdName(ps.getProductid().getProdname());
        }

        // Check for null supplierid
        if (ps.getSupplierid() != null) {
            dto.setSupplierId(ps.getSupplierid().getId());
            dto.setSupName(ps.getSupplierid().getSupname());
        }

        return dto;
    }

    //Creating new package
    @Transactional
    public Package createPackage(PackageRequestDTO request) {
        Package pkg = new Package();
        pkg.setPkgname(request.getPkgname());
        pkg.setPkgstartdate(request.getPkgstartdate());
        pkg.setPkgenddate(request.getPkgenddate());
        pkg.setPkgdesc(request.getPkgdesc());
        pkg.setPkgbaseprice(request.getPkgbaseprice());
        pkg.setPkgagencycommission(request.getPkgagencycommission());

        Package savedPackage = packageRepository.save(pkg);

        // Associate productsupplierids with the package
        List<Integer> productsupplierids = request.getProductsupplierids();
        for (Integer productsupplierid : productsupplierids) {
            ProductsSupplier productsSupplier = productsSupplierRepository.findById(String.valueOf(productsupplierid))
                    .orElseThrow(() -> new RuntimeException("ProductSupplier not found with id: " + productsupplierid));

            savedPackage.getProductsSuppliers().add(productsSupplier);
        }

        return packageRepository.save(savedPackage);
    }

    //updating the package
    @Transactional
    public Package updatePackage(Integer id, PackageRequestDTO request) {
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found with id: " + id));

        pkg.setPkgname(request.getPkgname());
        pkg.setPkgstartdate(request.getPkgstartdate());
        pkg.setPkgenddate(request.getPkgenddate());
        pkg.setPkgdesc(request.getPkgdesc());
        pkg.setPkgbaseprice(request.getPkgbaseprice());
        pkg.setPkgagencycommission(request.getPkgagencycommission());

        // Clear existing associations and add new ones
        pkg.getProductsSuppliers().clear();
        List<Integer> productsupplierids = request.getProductsupplierids();
        for (Integer productsupplierid : productsupplierids) {
            ProductsSupplier productsSupplier = productsSupplierRepository.findById(String.valueOf(productsupplierid))
                    .orElseThrow(() -> new RuntimeException("ProductSupplier not found with id: " + productsupplierid));

            pkg.getProductsSuppliers().add(productsSupplier);
        }

        return packageRepository.save(pkg);
    }

    //deleting the package
    @Transactional
    public void deletePackage(Integer id) {
        // Step 1: Find the package by ID
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found with id: " + id));

        // Step 2: Clear the associations in the bridging table
        pkg.getProductsSuppliers().clear();

        // Step 3: Delete the package
        packageRepository.delete(pkg);
    }
}