package org.example.travelexpertwebbackend.repository;

import org.example.travelexpertwebbackend.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Integer> {
    @Query("SELECT p FROM Package p WHERE " +
            "(COALESCE(:search, '') = '' OR LOWER(p.destination) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.pkgname) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:startDate IS NULL OR p.pkgstartdate >= :startDate) " +
            "AND (:endDate IS NULL OR p.pkgenddate <= :endDate) " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'price' AND :order = 'asc' THEN p.pkgbaseprice END ASC, " +
            "CASE WHEN :sortBy = 'price' AND :order = 'desc' THEN p.pkgbaseprice END DESC, " +
            "CASE WHEN :sortBy = 'rating' AND :order = 'asc' THEN (SELECT AVG(r.rating) FROM Ratings r WHERE r.packageEntity = p) END ASC, " +
            "CASE WHEN :sortBy = 'rating' AND :order = 'desc' THEN (SELECT AVG(r.rating) FROM Ratings r WHERE r.packageEntity = p) END DESC")
    List<Package> searchPackages(@Param("search") String search,
                                 @Param("sortBy") String sortBy,
                                 @Param("order") String order,
                                 @Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate);
}
