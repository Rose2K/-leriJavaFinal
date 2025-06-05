package com.medipol.javaFinal.util;

import com.medipol.javaFinal.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;


@Component
public class MultithreadedProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MultithreadedProcessor.class);
    private final ExecutorService executorService;

    public MultithreadedProcessor() {
        this.executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors(),
                r -> {
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    return t;
                });
    }


    public <T, R> List<Future<R>> processItemsInParallel(List<T> items, Function<T, R> processor) {
        return items.stream()
                .map(item -> executorService.submit(() -> processor.apply(item)))
                .collect(java.util.stream.Collectors.toList());
    }


    public List<Future<?>> processProductsInParallel(List<Product> products, Consumer<Product> action) {
        return products.stream()
                .map(product -> executorService.submit(() -> {
                    action.accept(product);
                    return null;
                }))
                .collect(java.util.stream.Collectors.toList());
    }


    public List<Product> updatePricesInParallel(List<Product> products, Function<Product, java.math.BigDecimal> updateFunction) {
        CountDownLatch latch = new CountDownLatch(products.size());

        // Her ürün için görevler oluşturma

        products.forEach(product -> executorService.submit(() -> {
            try {
                product.setPrice(updateFunction.apply(product));
            } catch (Exception e) {
                logger.error("Error updating price for product: " + product.getId(), e);
            } finally {
                latch.countDown();
            }
        }));

        // Tüm görevlerin tamamlanmasını ekleyin
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Interrupted while waiting for price updates", e);
        }

        return products;
    }


    public void processBatchesWithDelay(List<Product> products, Consumer<Product> action, int batchSize, long delayMs) {
        ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

        try {
            List<List<Product>> batches = splitIntoBatches(products, batchSize);

            for (int i = 0; i < batches.size(); i++) {
                final int batchIndex = i;
              
                scheduledExecutor.schedule(() -> {
                    logger.info("Processing batch {}", batchIndex);
                    processBatch(batches.get(batchIndex), action);
                    logger.info("Completed batch {}", batchIndex);
                }, i * delayMs, TimeUnit.MILLISECONDS);
            }

    
            scheduledExecutor.shutdown();
            scheduledExecutor.awaitTermination(products.size() * delayMs + 5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Batch processing interrupted", e);
        } finally {
            if (!scheduledExecutor.isShutdown()) {
                scheduledExecutor.shutdownNow();
            }
        }
    }

    private <T> List<List<T>> splitIntoBatches(List<T> items, int batchSize) {
        return java.util.stream.IntStream.range(0, (items.size() + batchSize - 1) / batchSize)
                .mapToObj(i -> items.subList(
                        i * batchSize,
                        Math.min((i + 1) * batchSize, items.size())))
                .collect(java.util.stream.Collectors.toList());
    }

    private <T> void processBatch(List<T> batch, Consumer<T> action) {
        batch.parallelStream().forEach(item -> {
            try {
                action.accept(item);
            } catch (Exception e) {
                logger.error("Error processing item in batch", e);
            }
        });
    }


    public <R> List<R> processWithCompletableFuture(List<Product> products, Function<Product, R> processor) {
        List<CompletableFuture<R>> futures = products.stream()
                .map(product -> CompletableFuture.supplyAsync(
                        () -> processor.apply(product),
                        executorService))
                .collect(java.util.stream.Collectors.toList());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0]));

        return allFutures.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(java.util.stream.Collectors.toList()))
                .join();
    }


    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
} 
