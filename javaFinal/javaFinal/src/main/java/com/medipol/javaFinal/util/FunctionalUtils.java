package com.medipol.javaFinal.util;

import com.medipol.javaFinal.model.Product;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class FunctionalUtils {


    public static List<Product> filterProducts(List<Product> products, Predicate<Product> predicate) {
        return products.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }


    public static List<Product> findProductsWithPriceHigherThan(List<Product> products, BigDecimal price) {
        return filterProducts(products, product -> product.getPrice().compareTo(price) > 0);
    }

 
    public static List<Product> findLowStockProducts(List<Product> products, int threshold) {
        return filterProducts(products, product -> product.getQuantity() < threshold);
    }

    public static BigDecimal calculateTotalInventoryValue(List<Product> products) {
        return products.stream()
                .map(product -> product.getPrice().multiply(new BigDecimal(product.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static Map<String, List<Product>> groupProductsByCategory(List<Product> products) {
        return products.stream()
                .filter(product -> product.getCategory() != null)
                .collect(Collectors.groupingBy(product -> product.getCategory().getName()));
    }


    public static List<Product> sortProducts(List<Product> products, Comparator<Product> comparator) {
        return products.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }


    public static List<Product> sortProductsByPriceAscending(List<Product> products) {
        return sortProducts(products, Comparator.comparing(Product::getPrice));
    }


    public static List<Product> sortProductsByPriceDescending(List<Product> products) {
        return sortProducts(products, Comparator.comparing(Product::getPrice).reversed());
    }

  

    public static <R> List<R> mapProducts(List<Product> products, Function<Product, R> mapper) {
        return products.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }


    public static Optional<Product> findMostExpensiveProduct(List<Product> products) {
        return products.stream()
                .max(Comparator.comparing(Product::getPrice));
    }
} 
