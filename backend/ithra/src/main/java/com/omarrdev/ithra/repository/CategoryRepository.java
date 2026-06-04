package com.omarrdev.ithra.repository;

import com.omarrdev.ithra.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByDeletedFalse();
    Optional<Category> findBySlugAndDeletedFalse(String slug);
    Optional<Category> findByIdAndDeletedFalse(Long id);
    boolean existsByNameAndDeletedFalse(String name);
    boolean existsBySlugAndDeletedFalse(String slug);
}
