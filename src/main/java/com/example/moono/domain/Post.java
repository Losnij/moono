package com.example.moono.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>(); // 댓글 리스트

    public void increment(){this.view++;}

    public void validateWriter(String memberID, String method) {
        if (!this.memberID.equals(memberID)) {
            throw new SecurityException("본인이 작성한 게시글만 " + method + " 할 수 있습니다.");
        }
    }

    public boolean sameMember(String memberID){
        return this.memberID.equals(memberID);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id != null && id.equals(post.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
