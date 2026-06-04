package com.omarrdev.ithra.repository;

import com.omarrdev.ithra.entity.Article;
import com.omarrdev.ithra.entity.ArticleLike;
import com.omarrdev.ithra.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    Optional<ArticleLike> findByArticleAndUser(Article article, User user);
    boolean existsByArticleAndUser(Article article, User user);
}
