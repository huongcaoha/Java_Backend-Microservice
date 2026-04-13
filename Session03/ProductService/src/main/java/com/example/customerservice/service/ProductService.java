package com.example.customerservice.service;

import com.example.customerservice.exception.CustomException;
import com.example.customerservice.model.dto.request.ProductRequestDTO;
import com.example.customerservice.model.entity.Product;
import com.example.customerservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product save(ProductRequestDTO productRequestDTO) {
        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setPrice(productRequestDTO.getPrice());
        product.setStockQuantity(productRequestDTO.getStockQuantity());
        return productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new CustomException("Product not found");
        }
        return product;
    }
}
