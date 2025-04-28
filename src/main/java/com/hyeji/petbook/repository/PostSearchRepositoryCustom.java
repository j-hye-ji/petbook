package com.hyeji.petbook.repository;

import com.hyeji.petbook.document.PostDocument;
import com.hyeji.petbook.dto.SearchRequest;
import org.springframework.data.domain.Page;

public interface PostSearchRepositoryCustom {
    Page<PostDocument> searchPosts(SearchRequest request);
}