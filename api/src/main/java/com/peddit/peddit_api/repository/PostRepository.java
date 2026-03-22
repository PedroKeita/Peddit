package com.peddit.peddit_api.repository;

import com.peddit.peddit_api.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository  extends JpaRepository<Post, Long> {

    @Query("""
 SELECT p FROM Post p
 WHERE (:communityId IS NULL OR p.community.id = :communityId)
""")
    Page<Post> findAllWithFilters(
            @Param("communityId") Long communityId,
            @Param("q") String q,
            Pageable pageable
    );
}
