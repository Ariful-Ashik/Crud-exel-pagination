package com.ashik.CrudProduct.service;

import com.ashik.CrudProduct.model.Product;
import com.ashik.CrudProduct.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> findAlls() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
   productRepository.deleteById(id);
    }
    @Override
    public Page<Product> searchProducts(String keyword, String status, Pageable pageable) {
        if (keyword != null && !keyword.isBlank() && status != null && !status.isBlank()) {
            return productRepository.findByNameContainingIgnoreCaseAndStatus(keyword, status, pageable);
        } else if (keyword != null && !keyword.isBlank()) {
            return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
        } else if (status != null && !status.isBlank()) {
            return productRepository.findByStatus(status, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }



    @Override
    public Page<Product> getPaginatedProducts(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }
}
