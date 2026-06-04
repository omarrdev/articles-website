package com.omarrdev.ithra.service;

import com.omarrdev.ithra.dto.request.CommentRequest;
import com.omarrdev.ithra.dto.response.CommentResponse;
import com.omarrdev.ithra.dto.response.PageResponse;
import com.omarrdev.ithra.entity.Article;
import com.omarrdev.ithra.entity.Comment;
import com.omarrdev.ithra.entity.User;
import com.omarrdev.ithra.exception.BusinessException;
import com.omarrdev.ithra.exception.ResourceNotFoundException;
import com.omarrdev.ithra.mapper.CommentMapper;
import com.omarrdev.ithra.repository.ArticleRepository;
import com.omarrdev.ithra.repository.CommentRepository;
import com.omarrdev.ithra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public PageResponse<CommentResponse> getComments(Long articleId, Pageable pageable) {
        Article article = articleRepository.findByIdAndDeletedFalse(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        Page<Comment> page = commentRepository.findByArticleAndDeletedFalse(article, pageable);
        return PageResponse.of(page, commentMapper::toResponse);
    }

    @Transactional
    public CommentResponse addComment(Long articleId, CommentRequest request) {
        User user = getCurrentUser();

        if (!user.isEmailVerified()) {
            throw new BusinessException("Email must be verified to post comments");
        }

        Article article = articleRepository.findByIdAndDeletedFalse(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .article(article)
                .author(user)
                .build();

        return commentMapper.toResponse(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(Long articleId, Long commentId) {
        Comment comment = commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        User currentUser = getCurrentUser();
        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");
        boolean isOwner = comment.getAuthor().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new BusinessException("You cannot delete this comment", HttpStatus.FORBIDDEN);
        }

        comment.setDeleted(true);
        commentRepository.save(comment);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new BusinessException("User not found", HttpStatus.UNAUTHORIZED));
    }
}
