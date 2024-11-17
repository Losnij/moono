package com.example.moono.domain;

import com.example.moono.dto.CommentResponseDto;

import java.util.ArrayList;
import java.util.List;

public class CommentList {
    private final List<Comment> commentList;

    public CommentList(List<Comment> comments) {
        this.commentList = new ArrayList<>(comments); // 방어적 복사
    }

    public List<CommentResponseDto> flattenCommentTree() {
        List<CommentResponseDto> result = new ArrayList<>();
        for (Comment comment : commentList) {
            recursiveFlattening(comment, 0, result);
        }
        return result;
    }

    private void recursiveFlattening(Comment comment, int depth, List<CommentResponseDto> result) {
        result.add(CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .depth(depth)
                .build());
        for (Comment reply : comment.getReplies()) {
            recursiveFlattening(reply, depth + 1, result);
        }
    }
}
