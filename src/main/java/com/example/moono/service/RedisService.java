package com.example.moono.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    // 조회 기록 갱신
    public void updateView(Long postId, String memberID) {
        String key = "post:" + postId + ":views";
        HashOperations<String, String, Boolean> hashOps = redisTemplate.opsForHash();

        hashOps.put(key, memberID, true);
        redisTemplate.expire(key, Duration.ofDays(1)); // TTL 하루
    }

    public boolean hasNotViewed(Long postId, String memberID) {
        String key = "post:" + postId + ":views";
        HashOperations<String, String, Boolean> hashOps = redisTemplate.opsForHash();
        return !hashOps.hasKey(key, memberID);
    }
}
