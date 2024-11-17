package com.example.moono.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private String content;
    private Long parentId;
}

