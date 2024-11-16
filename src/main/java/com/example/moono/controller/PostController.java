package com.example.moono.controller;

import com.example.moono.domain.Post;
import com.example.moono.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 등록
    @PostMapping
    public ResponseEntity<Post> createPost(HttpServletRequest request, @RequestBody Post post) {
        String memberID = (String) request.getAttribute("memberID");
        Post createdPost = postService.createPost(memberID, post.getTitle(), post.getContent());
        return ResponseEntity.ok(createdPost);
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<Post> updatePost(
            HttpServletRequest request,
            @PathVariable Long postId,
            @RequestBody Post post) {
        String memberID = (String) request.getAttribute("memberID");
        Post updatedPost = postService.updatePost(postId, memberID, post.getTitle(), post.getContent());
        return ResponseEntity.ok(updatedPost);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(HttpServletRequest request, @PathVariable Long postId) {
        String memberID = (String) request.getAttribute("memberID");
        postService.deletePost(postId, memberID);
        return ResponseEntity.ok("게시글을 삭제했습니다.");
    }

    // 게시글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable Long postId) {
        Post post = postService.getPost(postId);
        return ResponseEntity.ok(post);
    }
}
