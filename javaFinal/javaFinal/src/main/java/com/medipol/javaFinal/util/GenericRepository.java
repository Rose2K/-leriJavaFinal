package com.medipol.javaFinal.util;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Generic interface demonstrating generic programming
 * @param <T> Entity type
 * @param <ID> ID type of entity
 */
@NoRepositoryBean
public interface GenericRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    
    /**
     * Find entities by enabled flag
     * @param enabled enabled status
     * @return list of enabled/disabled entities
     */
    List<T> findByEnabled(boolean enabled);
    
    /**
     * Find entity by name
     * @param name name to search for
     * @return optional entity
     */
    Optional<T> findByName(String name);
} 