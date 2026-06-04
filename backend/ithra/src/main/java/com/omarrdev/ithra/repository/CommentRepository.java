package com.omarrdev.ithra.repository;

import com.omarrdev.ithra.entity.Article;
import com.omarrdev.ithra.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByArticleAndDeletedFalse(Article article, Pageable pageable);
    Optional<Comment> findByIdAndDeletedFalse(Long id);
    long countByDeletedFalse();
}
