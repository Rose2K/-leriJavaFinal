package com.medipol.javaFinal.util;

import com.medipol.javaFinal.model.Product;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class demonstrating Java 8 functional programming features
 */
public class FunctionalUtils {

    /**
     * Filter products by a given predicate
     * @param products list of products
     * @param predicate filter predicate
     * @return filtered list of products
     */
    public static List<Product> filterProducts(List<Product> products, Predicate<Product> predicate) {
        return products.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Find products with price higher than the given value
     * @param products list of products
     * @param price minimum price
     * @return filtered list of products
     */
    public static List<Product> findProductsWithPriceHigherThan(List<Product> products, BigDecimal price) {
        return filterProducts(products, product -> product.getPrice().compareTo(price) > 0);
    }

    /**
     * Find products with low stock
     * @param products list of products
     * @param threshold quantity threshold
     * @return filtered list of products
     */
    public static List<Product> findLowStockProducts(List<Product> products, int threshold) {
        return filterProducts(products, product -> product.getQuantity() < threshold);
    }

    /**
     * Calculate total inventory value
     * @param products list of products
     * @return total inventory value
     */
    public static BigDecimal calculateTotalInventoryValue(List<Product> products) {
        return products.stream()
                .map(product -> product.getPrice().multiply(new BigDecimal(product.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Group products by category name
     * @param products list of products
     * @return map of category name to list of products
     */
    public static Map<String, List<Product>> groupProductsByCategory(List<Product> products) {
        return products.stream()
                .filter(product -> product.getCategory() != null)
                .collect(Collectors.groupingBy(product -> product.getCategory().getName()));
    }

    /**
     * Sort products by a given comparator
     * @param products list of products
     * @param comparator comparator to sort by
     * @return sorted list of products
     */
    public static List<Product> sortProducts(List<Product> products, Comparator<Product> comparator) {
        return products.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /**
     * Sort products by price (ascending)
     * @param products list of products
     * @return sorted list of products
     */
    public static List<Product> sortProductsByPriceAscending(List<Product> products) {
        return sortProducts(products, Comparator.comparing(Product::getPrice));
    }

    /**
     * Sort products by price (descending)
     * @param products list of products
     * @return sorted list of products
     */
    public static List<Product> sortProductsByPriceDescending(List<Product> products) {
        return sortProducts(products, Comparator.comparing(Product::getPrice).reversed());
    }

    /**
     * Transform products to DTOs using a mapper function
     * @param products list of products
     * @param mapper mapper function
     * @param <R> target type
     * @return list of transformed objects
     */
    public static <R> List<R> mapProducts(List<Product> products, Function<Product, R> mapper) {
        return products.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * Find most expensive product
     * @param products list of products
     * @return optional of most expensive product
     */
    public static Optional<Product> findMostExpensiveProduct(List<Product> products) {
        return products.stream()
                .max(Comparator.comparing(Product::getPrice));
    }
} 