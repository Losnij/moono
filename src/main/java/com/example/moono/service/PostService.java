package com.example.moono.service;

import com.example.moono.domain.Post;
import com.example.moono.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 게시글 등록
    public Post createPost(String memberID, String title, String content) {
        Post post = Post.builder()
                .memberID(memberID)
                .title(title)
                .content(content)
                .build();
        return postRepository.save(post);
    }

    // 게시글 수정
    public Post updatePost(Long postId, String memberID, String title, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        if (!post.canBeModifiedBy(memberID)) {
            throw new SecurityException("본인이 작성한 게시글만 수정할 수 있습니다.");
        }
        Post updated = post.toBuilder()
                .title(title)
                .content(content)
                .build();
        return postRepository.save(updated);
    }

    // 게시글 삭제
    public void deletePost(Long postId, String memberID) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        if (!post.canBeModifiedBy(memberID)) {
            throw new SecurityException("본인이 작성한 게시글만 삭제할 수 있습니다.");
        }
        postRepository.delete(post);
    }

    // 게시글 조회
    public Post getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        post.increment(); // 조회수 증가
        return postRepository.save(post);
    }
}
