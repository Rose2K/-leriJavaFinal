package com.medipol.javaFinal.controller;

import com.medipol.javaFinal.model.Product;
import com.medipol.javaFinal.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "Operations related to products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

  @GetMapping
@Operation(summary = "Tüm ürünleri getir", description = "Sistemdeki tüm ürünlerin listesini döner")
public ResponseEntity<List<Product>> getAllProducts() {
    return ResponseEntity.ok(productService.getAllProducts());
}

@GetMapping("/{id}")
@Operation(summary = "ID'ye göre ürünü getir", description = "Belirtilen ID'ye sahip tek bir ürünü döner")
public ResponseEntity<Product> getProductById(@PathVariable Long id) {
    return productService.getProductById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}

@PostMapping
@Operation(summary = "Yeni ürün oluştur", description = "Sisteme yeni bir ürün ekler")
public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
    return new ResponseEntity<>(productService.saveProduct(product), HttpStatus.CREATED);
}

@PutMapping("/{id}")
@Operation(summary = "Ürünü güncelle", description = "Belirtilen ID'ye sahip mevcut bir ürünü günceller")
public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
    return productService.getProductById(id)
            .map(existingProduct -> {
                product.setId(id);
                return ResponseEntity.ok(productService.saveProduct(product));
            })
            .orElse(ResponseEntity.notFound().build());
}

@DeleteMapping("/{id}")
@Operation(summary = "Ürünü sil", description = "Belirtilen ID'ye sahip ürünü sistemden siler")
public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    if (productService.getProductById(id).isPresent()) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
}

@GetMapping("/category/{categoryId}")
@Operation(summary = "Kategoriye göre ürünleri getir", description = "Belirtilen kategoriye ait tüm ürünleri döner")
public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
    return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
}

@GetMapping("/search")
@Operation(summary = "İsme göre ürün ara", description = "İsmi arama terimini içeren ürünleri döner")
public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
    return ResponseEntity.ok(productService.searchProductsByName(name));
}

@GetMapping("/price-range")
@Operation(summary = "Fiyat aralığına göre ürünleri getir", description = "Belirtilen fiyat aralığındaki ürünleri döner")
public ResponseEntity<List<Product>> getProductsByPriceRange(
        @RequestParam BigDecimal minPrice,
        @RequestParam BigDecimal maxPrice) {
    return ResponseEntity.ok(productService.getProductsByPriceRange(minPrice, maxPrice));
}

@GetMapping("/low-stock")
@Operation(summary = "Düşük stoklu ürünleri getir", description = "Stok adedi 10'dan az olan tüm ürünleri döner")
public ResponseEntity<List<Product>> getLowStockProducts() {
    return ResponseEntity.ok(productService.getLowStockProducts());
}

@PatchMapping("/{id}/quantity")
@Operation(summary = "Ürün miktarını güncelle", description = "Belirli bir ürünün miktar bilgisini günceller")
public ResponseEntity<Void> updateProductQuantity(
        @PathVariable Long id,
        @RequestParam Integer quantity) {
    boolean updated = productService.updateProductQuantity(id, quantity);
    return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
}
