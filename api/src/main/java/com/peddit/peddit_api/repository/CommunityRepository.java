package com.peddit.peddit_api.repository;

import com.peddit.peddit_api.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    boolean existsByName(String name);

    @Query("SELECT c FROM Community c LEFT JOIN FETCH c.posts")
    Page<Community> findAllWithPostCount(Pageable pageable);
}
