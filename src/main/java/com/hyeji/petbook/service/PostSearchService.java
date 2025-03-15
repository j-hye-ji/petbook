package com.hyeji.petbook.service;

import com.hyeji.petbook.entity.Post;
import com.hyeji.petbook.document.PostDocument;
import com.hyeji.petbook.repository.PostRepository;
import com.hyeji.petbook.repository.PostSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostSearchService {

    private final PostRepository postRepository;
    private final PostSearchRepository postSearchRepository;

    /* MySQL 데이터 -> ElasticSearch로 동기화 */
    public void synchronizeMysqlToES() {
        List<Post> posts = postRepository.findAll();
        List<PostDocument> postDocuments = posts.stream().map(post -> new PostDocument(
                post.getId().toString(),
                post.getTitle(),
                post.getContents()
        )).collect(Collectors.toList());

        postSearchRepository.saveAll(postDocuments);
    }

    // ElasticSearch로 게시글 검색
    public List<PostDocument> searchPosts(String keyword) {
        return postSearchRepository.findByTitleContaining(keyword);
    }
}
