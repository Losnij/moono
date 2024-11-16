package com.example.moono.dto;

import com.example.moono.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDto {

    private Long id;
    private String memberID;
    private String title;
    private String content;
    private int view;

    public static PostDto fromEntity(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .memberID(post.getMemberID())
                .title(post.getTitle())
                .content(post.getContent())
                .view(post.getView())
                .build();
    }
}
