package com.example.moono.dto;

import com.example.moono.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CommentResponseDto {
    private Long id; // 댓글 ID
    private String content; // 댓글 내용
    private int depth; // 댓글의 깊이

    public static CommentResponseDto fromEntity(Comment comment, int depth) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .depth(depth)
                .build();
    }
}
