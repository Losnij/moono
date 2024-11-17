package com.example.moono.dto;

import com.example.moono.domain.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {
    private Long id;
    private String content;
    private int depth;

    public static CommentResponseDto fromEntity(Comment comment, int depth) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .depth(depth)
                .build();
    }
}
