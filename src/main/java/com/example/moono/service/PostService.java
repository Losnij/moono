package com.example.moono.service;

import com.example.moono.domain.Post;
import com.example.moono.dto.PostResponseDto;
import com.example.moono.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final RedisService redisService;
    private final RestTemplate restTemplate;

    // 게시글 등록
    @Transactional
    public Post createPost(String memberID, String title, String content) {
        Post post = Post.builder()
                .memberID(memberID)
                .title(title)
                .content(content)
                .build();
        return postRepository.save(post);
    }

    // 게시글 수정
    @Transactional
    public Post updatePost(Long postId, String memberID, String title, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        post.validateWriter(memberID, "수정");
        Post updated = post.toBuilder()
                .title(title)
                .content(content)
                .build();
        return postRepository.save(updated);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long postId, String memberID) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        post.validateWriter(memberID, "삭제");
        postRepository.delete(post);
    }

    // 게시글 조회
    @Transactional
    public Post getPost(Long postId, String memberID) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (post.sameMember(memberID)) {
            return post;
        }

        if (redisService.hasNotViewed(postId, memberID)) { // 하루 이내 조회 기록이 없는 경우
            redisService.updateView(postId, memberID);
            post.viewIncrement();
            postRepository.save(post);
        }

        return post;
    }

    // 게시글 리스트 조회
    @Transactional
    public List<PostResponseDto> getPostsByPage(int page) {
        Pageable pageable = PageRequest.of(page - 1, 5);
        Page<Post> postPage = postRepository.findAll(pageable);
        return postPage.getContent().stream()
                .map(PostResponseDto::fromEntity)
                .toList();
    }

    // 외부 게시글 일괄 등록
    @Transactional
    public int importPosts(String memberID, String url) {
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        List<Map<String, Object>> posts = (List<Map<String, Object>>) response.getBody().get("posts");

        List<Post> postEntities = posts.stream()
                .map(post -> {
                    Map<String, Object> source = (Map<String, Object>) post.get("_source");
                    return Post.builder()
                            .memberID(memberID)
                            .title((String) source.get("title"))
                            .content((String) source.get("id"))
                            .build();
                })
                .toList();

        postRepository.saveAll(postEntities);
        return postEntities.size();
    }
}
