package com.example.moono.config;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import redis.embedded.RedisServer;

import java.io.IOException;
import java.net.Socket;

@Configuration
public class EmbeddedRedisConfig {

    private static final int REDIS_PORT = 6379;
    private RedisServer redisServer;

    @Bean
    public RedisServer redisServer() throws IOException {
        if (needToRunRedis()) {
            redisServer = new RedisServer(REDIS_PORT);
            redisServer.start();
        }
        return redisServer;
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null && redisServer.isActive()) {
            redisServer.stop();
        }
    }

    private boolean needToRunRedis() {
        try (Socket socket = new Socket("localhost", REDIS_PORT)) {
            return false; // Redis가 실행 중
        } catch (IOException e) {
            return true; // Redis가 실행 중 아님
        }
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
