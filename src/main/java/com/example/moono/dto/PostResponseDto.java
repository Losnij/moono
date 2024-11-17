package com.example.moono.dto;

import com.example.moono.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponseDto {

    private Long id;
    private String memberID;
    private String title;
    private String content;
    private int view;

    public static PostResponseDto fromEntity(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .memberID(post.getMemberID())
                .title(post.getTitle())
                .content(post.getContent())
                .view(post.getView())
                .build();
    }
}
