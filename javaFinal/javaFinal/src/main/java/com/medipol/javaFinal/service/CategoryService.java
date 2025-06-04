package com.medipol.javaFinal.service;

import com.medipol.javaFinal.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    
    List<Category> getAllCategories();
    
    Optional<Category> getCategoryById(Long id);
    
    Optional<Category> getCategoryByName(String name);
    
    Category saveCategory(Category category);
    
    void deleteCategory(Long id);
    
    List<Category> searchCategoriesByName(String name);
    
    boolean existsByName(String name);
} 