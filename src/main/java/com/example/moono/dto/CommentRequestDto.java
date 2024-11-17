package com.example.moono.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CommentRequestDto {
    private String content; // 댓글 내용
    private Long parentId;  // 부모 댓글 ID
}

