package com.example.moono.service;

import com.example.moono.domain.Post;
import com.example.moono.dto.PostResponseDto;
import com.example.moono.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final RedisService redisService;

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

        LocalDateTime viewTime = redisService.getViewTime(postId, memberID);
        if (viewTime == null || viewTime.isBefore(LocalDateTime.now().minusHours(24))) {
            // 첫 조회거나 하루가 지난 경우
            redisService.updateView(postId, memberID);
            post.increment(); // 조회수 증가
            postRepository.save(post); // DB 업데이트
        }

        return post;
    }

    // 게시글 리스트 조회
    public List<PostResponseDto> getPostsByPage(int page) {
        Pageable pageable = PageRequest.of(page - 1, 5);
        Page<Post> postPage = postRepository.findAll(pageable);
        return postPage.getContent().stream()
                .map(PostResponseDto::fromEntity)
                .toList();
    }
}
