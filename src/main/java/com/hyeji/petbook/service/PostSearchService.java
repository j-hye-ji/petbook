package com.hyeji.petbook.service;

import com.hyeji.petbook.dto.SearchRequest;
import com.hyeji.petbook.entity.Post;
import com.hyeji.petbook.document.PostDocument;
import com.hyeji.petbook.repository.PostRepository;
import com.hyeji.petbook.repository.PostSearchRepositoryImpl;
import com.hyeji.petbook.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostSearchService {

//    private final PostRepository postRepository;
    private final PostSearchRepositoryImpl postSearchRepositoryImpl;

    public PageResponse<PostDocument> searchPosts(SearchRequest request) {
        try {
            // 1. Repository 호출해서 검색 실행
            Page<PostDocument> postPage = postSearchRepositoryImpl.searchPosts(request);

            // 2. Page<PostDocument>를 PageResponse<PostDocument>로 변환
            return PageResponse.<PostDocument>builder()
                    .content(postPage.getContent()) // 데이터 목록
                    .page(postPage.getNumber()) // 현재 페이지 번호
                    .size(postPage.getSize()) // 페이지당 데이터 수
                    .hasNext(postPage.hasNext()) // 다음 페이지 존재 여부
                    .totalElements(postPage.getTotalElements()) // 전체 문서 개수
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("게시글 검색에 실패했습니다.", e);
        }
    }

//    /* MySQL 데이터 -> ElasticSearch로 동기화 */
//    public void synchronizeMysqlToES() {
//        List<Post> posts = postRepository.findAll();
//        List<PostDocument> postDocuments = posts.stream().map(post -> new PostDocument(
//                post.getId().toString(),
//                post.getTitle(),
//                post.getContents()
//        )).collect(Collectors.toList());
//
//        postSearchRepository.saveAll(postDocuments);
//    }
//
//    // ElasticSearch로 게시글 검색
//    public List<PostDocument> searchPosts(String keyword) {
//        return postSearchRepository.findByTitleContaining(keyword);
//    }
}
