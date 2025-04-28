package com.hyeji.petbook.controller;

import com.hyeji.petbook.document.PostDocument;
import com.hyeji.petbook.document.StoreDocument;
import com.hyeji.petbook.dto.SearchRequest;
import com.hyeji.petbook.response.PageResponse;
import com.hyeji.petbook.service.PostSearchService;
import com.hyeji.petbook.service.StoreSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

//    // ElasticSearch로 게시글 검색 (키워드만으로도 가능)
//    @GetMapping("/posts")
//    public ResponseEntity<List<PostDocument>> searchPosts(@RequestParam String keyword) {
//        List<PostDocument> postDocuments = postSearchService.searchPosts(keyword);
//        return ResponseEntity.ok(postDocuments);
//    }

    /* ElasticSearch로 게시글 검색 (코드 리팩토링) */
    @PostMapping("/posts")
    public ResponseEntity<PageResponse<PostDocument>> searchPosts(@RequestBody SearchRequest request) {
        PageResponse<PostDocument> response = postSearchService.searchPosts(request);
        return ResponseEntity.ok(response);
    }
}
