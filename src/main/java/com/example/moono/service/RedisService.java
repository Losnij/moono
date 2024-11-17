package com.example.moono.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 조회 기록 갱신
    public void updateView(Long postId, String memberID) {
        String key = "post:" + postId + ":views";
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        hashOps.put(key, memberID, LocalDateTime.now().format(formatter));
    }

    // 조회수가 오른 시점의 조회 시간 가져오기
    public LocalDateTime getViewTime(Long postId, String memberID) {
        String key = "post:" + postId + ":views";
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        String viewTime = hashOps.get(key, memberID); // memberID 계정이 조회수에 유효하게 조회한 시간
        return viewTime != null ? LocalDateTime.parse(viewTime, formatter) : null;
    }
}
