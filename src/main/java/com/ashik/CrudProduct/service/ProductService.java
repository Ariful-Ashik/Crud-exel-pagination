package com.ashik.CrudProduct.service;

import com.ashik.CrudProduct.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<Product> findAlls();

    Product findById(Long id);

    Product save(Product product);

    void delete(Long id);

   Page<Product> getPaginatedProducts(int page, int size);
    Page<Product> searchProducts(String keyword, String status, Pageable pageable);

//    Page<Product> searchProducts(String keyword, Pageable pageable);

}
