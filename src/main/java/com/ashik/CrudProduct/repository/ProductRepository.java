package com.ashik.CrudProduct.repository;

import com.ashik.CrudProduct.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByStatus(String status, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCaseAndStatus(String name, String status, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseOrStatusContainingIgnoreCase(String name, String status, Pageable pageable);

}
