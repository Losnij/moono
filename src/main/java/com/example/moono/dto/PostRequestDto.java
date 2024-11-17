package com.example.moono.dto;

import com.example.moono.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostRequestDto {

    private String memberID;
    private String title;
    private String content;
    private int view;

    public Post toEntity() {
        return Post.builder()
                .memberID(this.getMemberID())
                .title(this.getTitle())
                .content(this.getContent())
                .view(this.getView())
                .build();
    }
}
