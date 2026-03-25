package com.peddit.peddit_api.repository;

import com.peddit.peddit_api.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    @Query("""
    SELECT DISTINCT c FROM Comment c
    JOIN FETCH c.author
    LEFT JOIN FETCH c.replies r
    LEFT JOIN FETCH r.author
    WHERE c.post.id = :postId
    AND c.parent IS NULL
    ORDER BY c.createdAt DESC
    """)
    List<Comment> findTopLevelCommentsWithAuthor(Long postId);
}
