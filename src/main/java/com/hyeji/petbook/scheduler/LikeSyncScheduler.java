package com.hyeji.petbook.scheduler;

import com.hyeji.petbook.entity.Post;
import com.hyeji.petbook.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeSyncScheduler {

    private final PostRepository postRepository;
    private final StringRedisTemplate redisTemplate;

    private static final String POST_LIKE_COUNT_KEY_PREFIX = "post:like:count:";

    // 좋아요 수 동기화 (Redis -> DB)
    @Scheduled(fixedRate = 5 * 60 * 1000) // 5분마다 실행
    public void syncLikeCountsToDB() {
        log.info("[Scheduler] 좋아요 수 동기화 시작");

        Set<String> keys = redisTemplate.keys(POST_LIKE_COUNT_KEY_PREFIX + "*");

        if (keys == null || keys.isEmpty()) {
            log.info("[Scheduler] 동기화할 수 없음");
            return;
        }

        for (String key : keys) {
            try {
                String strPostId = key.replace(POST_LIKE_COUNT_KEY_PREFIX, "");
                Long postId = Long.parseLong(strPostId);

                String strLikeCount = redisTemplate.opsForValue().get(key);
                if (strLikeCount == null) continue;

                int likeCount = Integer.parseInt(strLikeCount);

                Post post = postRepository.findById(postId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

                post.setLikesCount(likeCount);
                postRepository.save(post);

                log.info("[Scheduler] 게시글 {} 좋아요 수 {} 로 동기화 완료", postId, likeCount);

            } catch (Exception e) {
                log.error("[Scheduler] 좋아요 동기화 실패 key = {}, 이유 = {}", key, e.getMessage());
            }
        }
        log.info("[Scheduler] 좋아요 수 동기화 종료");
    }
}
