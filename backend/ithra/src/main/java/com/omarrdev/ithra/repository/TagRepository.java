package com.omarrdev.ithra.repository;

import com.omarrdev.ithra.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAll();
    Optional<Tag> findBySlug(String slug);
    Optional<Tag> findByIdAndSlugNotNull(Long id);
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
}
