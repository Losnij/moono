package com.example.moono.service;

import com.example.moono.domain.Comment;
import com.example.moono.domain.Post;
import com.example.moono.repository.CommentRepository;
import com.example.moono.repository.PostRepository;
import com.example.moono.dto.CommentRequestDto;
import com.example.moono.dto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    // 댓글 추가
    public void addComment(Long postId, CommentRequestDto commentRequestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("유효한 게시글이 아닙니다."));

        Comment parent = null;
        if (commentRequestDto.getParentId() != null) {
            parent = commentRepository.findById(commentRequestDto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("유효한 댓글이 아닙니다."));
        }

        Comment comment = Comment.builder()
                .content(commentRequestDto.getContent())
                .post(post)
                .parent(parent)
                .build();
        post.addComment(comment);
        if(parent != null) {
            parent.addReply(comment);
        }
        commentRepository.save(comment);
    }

    // 특정 게시글의 모든 댓글과 대댓글 조회
    public List<CommentResponseDto> getAllCommentsOfPost(Long postId) {
        List<Comment> rootComments = commentRepository.findByPostIdAndParentIsNull(postId);
        List<CommentResponseDto> flattenedComments = new ArrayList<>();
        for (Comment root : rootComments) {
            flattenCommentTree(root, 0, flattenedComments);
        }
        return flattenedComments;
    }

    // 특정 댓글과 그 하단에 있는 모든 대댓글 조회
    public List<CommentResponseDto> getCommentsOfComment(Long postId, Long commentId) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        List<CommentResponseDto> flattenedComments = new ArrayList<>();
        flattenCommentTree(comment, 0, flattenedComments);
        return flattenedComments;
    }

    // 대댓글 우선으로 depth 기록 후 순차적 출력
    private void flattenCommentTree(Comment comment, int depth, List<CommentResponseDto> result) {
        result.add(CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .depth(depth)
                .build());
        for (Comment reply : comment.getReplies()) {
            flattenCommentTree(reply, depth + 1, result);
        }
    }
}
