package com.medipol.javaFinal.service;

import com.medipol.javaFinal.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

/**
 * Service that implements various sorting and search algorithms
 */
@Service
public class SortingService {

    /**
     * Merge sort implementation for a list of products
     * @param products list of products to sort
     * @param comparator comparator to sort by
     * @return sorted list of products
     */
    public List<Product> mergeSortProducts(List<Product> products, Comparator<Product> comparator) {
        if (products.size() <= 1) {
            return new ArrayList<>(products);
        }

        List<Product> result = new ArrayList<>(products);
        mergeSort(result, 0, result.size() - 1, comparator);
        return result;
    }

    private void mergeSort(List<Product> products, int left, int right, Comparator<Product> comparator) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            mergeSort(products, left, mid, comparator);
            mergeSort(products, mid + 1, right, comparator);

            merge(products, left, mid, right, comparator);
        }
    }

    private void merge(List<Product> products, int left, int mid, int right, Comparator<Product> comparator) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        List<Product> leftList = new ArrayList<>();
        List<Product> rightList = new ArrayList<>();

        for (int i = 0; i < n1; i++) {
            leftList.add(products.get(left + i));
        }
        for (int i = 0; i < n2; i++) {
            rightList.add(products.get(mid + 1 + i));
        }

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (comparator.compare(leftList.get(i), rightList.get(j)) <= 0) {
                products.set(k, leftList.get(i));
                i++;
            } else {
                products.set(k, rightList.get(j));
                j++;
            }
            k++;
        }

        while (i < n1) {
            products.set(k, leftList.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            products.set(k, rightList.get(j));
            j++;
            k++;
        }
    }

    /**
     * Quick sort implementation for a list of products
     * @param products list of products to sort
     * @param comparator comparator to sort by
     * @return sorted list of products
     */
    public List<Product> quickSortProducts(List<Product> products, Comparator<Product> comparator) {
        if (products.size() <= 1) {
            return new ArrayList<>(products);
        }

        List<Product> result = new ArrayList<>(products);
        quickSort(result, 0, result.size() - 1, comparator);
        return result;
    }

    private void quickSort(List<Product> products, int low, int high, Comparator<Product> comparator) {
        if (low < high) {
            int pivotIndex = partition(products, low, high, comparator);
            quickSort(products, low, pivotIndex - 1, comparator);
            quickSort(products, pivotIndex + 1, high, comparator);
        }
    }

    private int partition(List<Product> products, int low, int high, Comparator<Product> comparator) {
        Product pivot = products.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparator.compare(products.get(j), pivot) <= 0) {
                i++;
                Collections.swap(products, i, j);
            }
        }

        Collections.swap(products, i + 1, high);
        return i + 1;
    }

    /**
     * Binary search implementation for products
     * @param products list of products (must be sorted already)
     * @param target target product
     * @param comparator comparator to compare products
     * @return index of the product or -1 if not found
     */
    public int binarySearch(List<Product> products, Product target, Comparator<Product> comparator) {
        int left = 0;
        int right = products.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparison = comparator.compare(products.get(mid), target);

            if (comparison == 0) {
                return mid;
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;
    }

    /**
     * Binary search for product by price
     * @param products list of products (must be sorted by price)
     * @param targetPrice price to search for
     * @return index of product with closest price or -1 if list is empty
     */
    public int binarySearchByPrice(List<Product> products, BigDecimal targetPrice) {
        if (products.isEmpty()) {
            return -1;
        }

        List<Product> sortedProducts = products.stream()
                .sorted(Comparator.comparing(Product::getPrice))
                .toList();

        int left = 0;
        int right = sortedProducts.size() - 1;
        int closest = -1;
        BigDecimal closestDifference = null;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            BigDecimal currentPrice = sortedProducts.get(mid).getPrice();
            int comparison = currentPrice.compareTo(targetPrice);

            // Update closest match
            BigDecimal currentDifference = currentPrice.subtract(targetPrice).abs();
            if (closestDifference == null || currentDifference.compareTo(closestDifference) < 0) {
                closestDifference = currentDifference;
                closest = mid;
            }

            if (comparison == 0) {
                return mid;
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return closest;
    }

    /**
     * Bucket sort implementation for products by price range
     * @param products list of products to sort
     * @param minPrice minimum price in the data set
     * @param maxPrice maximum price in the data set
     * @param bucketCount number of buckets to use
     * @return sorted list of products
     */
    public List<Product> bucketSortByPrice(List<Product> products, BigDecimal minPrice, BigDecimal maxPrice, int bucketCount) {
        if (products.isEmpty()) {
            return new ArrayList<>();
        }

        // Create buckets
        List<List<Product>> buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }

        // Calculate the range for each bucket
        BigDecimal range = maxPrice.subtract(minPrice);
        Function<Product, Integer> getBucketIndex = product -> {
            if (range.compareTo(BigDecimal.ZERO) == 0) {
                return 0;
            }
            BigDecimal normalized = product.getPrice().subtract(minPrice);
            double ratio = normalized.doubleValue() / range.doubleValue();
            int bucketIdx = (int) (ratio * (bucketCount - 1));
            return Math.min(Math.max(bucketIdx, 0), bucketCount - 1);
        };

        // Place products in buckets
        for (Product product : products) {
            int bucketIdx = getBucketIndex.apply(product);
            buckets.get(bucketIdx).add(product);
        }

        // Sort each bucket and concatenate
        List<Product> result = new ArrayList<>(products.size());
        for (List<Product> bucket : buckets) {
            bucket.sort(Comparator.comparing(Product::getPrice));
            result.addAll(bucket);
        }

        return result;
    }
} 