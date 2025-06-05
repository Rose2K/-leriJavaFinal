package com.medipol.javaFinal.controller;

import com.medipol.javaFinal.model.Category;
import com.medipol.javaFinal.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category API", description = "Kategorilerle ilgili işlemler")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Tüm kategorileri Getir", description = "Sistemdeki tüm kategorilerin listesini döndürür")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Id ye göre kategori getir", description = "Tek bir kategoriyi kimliğine göre döndürür")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Ada göre kategori getir", description = "Adına göre tek bir kategori döndürür")
    public ResponseEntity<Category> getCategoryByName(@PathVariable String name) {
        return categoryService.getCategoryByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Yeni kategori oluştur", description = "Sistemde yeni bir kategori oluşturur")
    public ResponseEntity<?> createCategory(@Valid @RequestBody Category category) {
        if (categoryService.existsByName(category.getName())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Category with name '" + category.getName() + "' already exists");
        }
        return new ResponseEntity<>(categoryService.saveCategory(category), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Kategoriyi güncelle", description = "ID ye göre kategori güncelle")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody Category category) {
        return categoryService.getCategoryById(id)
                .map(existingCategory -> {
                    if (!existingCategory.getName().equals(category.getName()) && 
                            categoryService.existsByName(category.getName())) {
                        return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body("Category with name '" + category.getName() + "' already exists");
                    }
                    category.setId(id);
                    return ResponseEntity.ok(categoryService.saveCategory(category));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Kategoriyi Sil", description = "Id ye göre kategori sil")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (categoryService.getCategoryById(id).isPresent()) {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Ada göre kategori ara", description = "Adında arama terimini içeren kategorileri döndürür")
    public ResponseEntity<List<Category>> searchCategories(@RequestParam String name) {
        return ResponseEntity.ok(categoryService.searchCategoriesByName(name));
    }
} 
