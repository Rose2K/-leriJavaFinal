package com.medipol.javaFinal.service;

import com.medipol.javaFinal.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    
    List<Product> getAllProducts();
    
    Optional<Product> getProductById(Long id);
    
    Product saveProduct(Product product);
    
    void deleteProduct(Long id);
    
    List<Product> getProductsByCategory(Long categoryId);
    
    List<Product> searchProductsByName(String name);
    
    List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    List<Product> getLowStockProducts();
    
    boolean updateProductQuantity(Long id, Integer quantity);
} 