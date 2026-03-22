package com.peddit.peddit_api.repository;

import com.peddit.peddit_api.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    @Query("""
    SELECT c FROM Comment c
    JOIN FETCH c.author
    WHERE c.post.id = :postId
    AND c.parent IS NULL
    ORDER BY c.createdAt DESC
""")
    List<Comment> findTopLevelCommentsWithAuthor(Long postId);
}
