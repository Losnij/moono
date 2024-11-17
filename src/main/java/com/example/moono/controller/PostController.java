package com.example.moono.controller;

import com.example.moono.domain.Post;
import com.example.moono.dto.ExternalLinkDto;
import com.example.moono.dto.PostRequestDto;
import com.example.moono.dto.PostResponseDto;
import com.example.moono.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 등록
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(HttpServletRequest request, @RequestBody PostRequestDto postRequestDto) {
        Post post = postRequestDto.toEntity();
        String memberID = (String) request.getAttribute("memberID");
        PostResponseDto response = PostResponseDto.fromEntity(postService.createPost(memberID, post.getTitle(), post.getContent()));
        return ResponseEntity.ok(response);
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            HttpServletRequest request,
            @PathVariable Long postId,
            @RequestBody PostRequestDto postRequestDto) {
        Post post = postRequestDto.toEntity();
        String memberID = (String) request.getAttribute("memberID");
        PostResponseDto response = PostResponseDto.fromEntity(postService.updatePost(postId, memberID, post.getTitle(), post.getContent()));
        return ResponseEntity.ok(response);
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
    public ResponseEntity<PostResponseDto> getPost(HttpServletRequest request, @PathVariable Long postId) {
        String memberID = (String) request.getAttribute("memberID");
        PostResponseDto response = PostResponseDto.fromEntity(postService.getPost(postId, memberID));
        return ResponseEntity.ok(response);
    }

    // 게시글 리스트 조회
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getPostsByPage(@RequestParam int page) {
        List<PostResponseDto> postResponseDtoList = postService.getPostsByPage(page);
        return ResponseEntity.ok(postResponseDtoList);
    }

    // 외부 게시글 일괄 등록
    @PostMapping("/import")
    public ResponseEntity<String> importPosts(HttpServletRequest request,
                                              @RequestBody ExternalLinkDto externalLinkDto) {
        String memberID = (String) request.getAttribute("memberID");
        String url = externalLinkDto.getUrl();
        int importedCount = postService.importPosts(memberID, url);
        return ResponseEntity.ok(importedCount + "개의 게시글이 등록되었습니다.");
    }
}
