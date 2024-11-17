package com.example.moono.controller;

import com.example.moono.domain.Comment;
import com.example.moono.dto.CommentRequestDto;
import com.example.moono.dto.CommentResponseDto;
import com.example.moono.repository.CommentRepository;
import com.example.moono.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;

    // 댓글 추가
    @PostMapping("/{postId}/comments")
    public ResponseEntity<String> addComment(@PathVariable Long postId,
                                             @RequestBody CommentRequestDto commentRequestDto) {
        commentService.addComment(postId, commentRequestDto);
        return ResponseEntity.ok("댓글이 추가되었습니다.");
    }

    // 특정 게시글의 모든 댓글과 대댓글 조회
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getAllCommentsOfPost(@PathVariable Long postId) {
        List<CommentResponseDto> comments = commentService.getAllCommentsOfPost(postId);
        return ResponseEntity.ok(comments);
    }

    // 특정 댓글과 그 하단에 있는 모든 대댓글 조회
    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsOfComment(@PathVariable Long postId, @PathVariable Long commentId) {
        List<CommentResponseDto> comments = commentService.getCommentsOfComment(postId, commentId);
        return ResponseEntity.ok(comments);
    }
}
