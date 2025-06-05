package com.medipol.javaFinal.util;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface GenericRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    

    List<T> findByEnabled(boolean enabled);
    

    Optional<T> findByName(String name);
} 
