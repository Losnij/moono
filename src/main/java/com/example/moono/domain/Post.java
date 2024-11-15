package com.example.moono.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder(toBuilder = true)
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @Column(nullable = false)
    private String memberID; // 작성자 memberID

    @Column(nullable = false)
    private String title; // 게시글 제목

    @Column(nullable = false)
    private String content; // 게시글 본문

    @Column(nullable = false)
    @Builder.Default
    private int view = 0; // 게시글 조회수

    public void increment(){this.view++;}

    public boolean canBeModifiedBy(String memberID){
        return this.memberID.equals(memberID);
    }
}
