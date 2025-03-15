package com.hyeji.petbook.repository;

import com.hyeji.petbook.document.StoreDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreSearchRepository extends ElasticsearchRepository<StoreDocument, String> {
    List<StoreDocument> findByStoreNameContaining(String keyword);
}
