package com.hyeji.petbook.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.hyeji.petbook.document.PostDocument;
import com.hyeji.petbook.dto.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PostSearchRepositoryImpl implements PostSearchRepositoryCustom {

    private final ElasticsearchClient elasticsearchClient;

    @Override
    public Page<PostDocument> searchPosts(SearchRequest request) {
        try {
            // 1. 검색 쿼리 실행
            SearchResponse<PostDocument> search = elasticsearchClient.search(s -> s
                            .index("post_index")
                            .query(q -> q
                                    .multiMatch(m -> m
                                            .fields("title", "contents")
                                            .query(request.getKeyword())
                                            .type(TextQueryType.BestFields)
                                    )
                            ),
                    PostDocument.class);

            // 2. 검색 결과를 List<PostDocument>로 변환
            List<PostDocument> content = search.hits().hits().stream().map(Hit::source).collect(Collectors.toList());

            // total 개수 null-safe하게 처리
            long totalHits = search.hits().total() != null ? search.hits().total().value() : 0L;

            // 3. List를 Spring Data의 Page 객체로 감싸서 반환
            return new PageImpl<> (
                    content, PageRequest.of(request.getPage(), request.getSize()),
                    totalHits
            );

        } catch (IOException e) {
            // 4. Elasticsearch 통신 실패 시 예외 처리
            throw new RuntimeException("검색 실패", e);
        }


    }
}
