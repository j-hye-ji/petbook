package com.hyeji.petbook.controller;

import com.hyeji.petbook.entity.PostDocument;
import com.hyeji.petbook.entity.StoreDocument;
import com.hyeji.petbook.service.PostSearchService;
import com.hyeji.petbook.service.StoreSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final PostSearchService postSearchService;
    private final StoreSearchService storeSearchService;

    // ElasticSearch로 매장 검색 (키워드만으로도 가능)
    @GetMapping("/stores")
    public ResponseEntity<List<StoreDocument>> searchStores(@RequestParam String keyword) {
        List<StoreDocument> storeDocuments = storeSearchService.searchStores(keyword);
        return ResponseEntity.ok(storeDocuments);
    }

    // ElasticSearch로 게시글 검색 (키워드만으로도 가능)
    @GetMapping("/posts")
    public ResponseEntity<List<PostDocument>> searchPosts(@RequestParam String keyword) {
        List<PostDocument> postDocuments = postSearchService.searchPosts(keyword);
        return ResponseEntity.ok(postDocuments);
    }
}
