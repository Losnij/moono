package com.example.moono.repository;

import com.example.moono.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글의 최상위 댓글(부모가 없는 댓글) 조회
    @EntityGraph(attributePaths = {"replies"}) // N + 1 문제 방지
    List<Comment> findByPostIdAndParentIsNull(Long postId);

    Optional<Comment> findByIdAndPostId(Long commentId, Long postId);
}
