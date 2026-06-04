package com.omarrdev.ithra.service;

import com.omarrdev.ithra.dto.request.TagRequest;
import com.omarrdev.ithra.dto.response.TagResponse;
import com.omarrdev.ithra.entity.Tag;
import com.omarrdev.ithra.exception.BusinessException;
import com.omarrdev.ithra.exception.ResourceNotFoundException;
import com.omarrdev.ithra.mapper.TagMapper;
import com.omarrdev.ithra.repository.TagRepository;
import com.omarrdev.ithra.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public List<TagResponse> getAll() {
        return tagMapper.toResponseList(tagRepository.findAll());
    }

    public TagResponse getBySlug(String slug) {
        return tagMapper.toResponse(
                tagRepository.findBySlug(slug)
                        .orElseThrow(() -> new ResourceNotFoundException("Tag not found: " + slug))
        );
    }

    @Transactional
    public TagResponse create(TagRequest request) {
        if (tagRepository.existsByName(request.getName())) {
            throw new BusinessException("Tag already exists");
        }
        Tag tag = Tag.builder()
                .name(request.getName())
                .slug(SlugUtil.toSlug(request.getName()))
                .build();
        return tagMapper.toResponse(tagRepository.save(tag));
    }

    @Transactional
    public TagResponse update(Long id, TagRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        tag.setName(request.getName());
        tag.setSlug(SlugUtil.toSlug(request.getName()));
        return tagMapper.toResponse(tagRepository.save(tag));
    }

    @Transactional
    public void delete(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
        tagRepository.delete(tag);
    }
}
