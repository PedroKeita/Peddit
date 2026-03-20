package com.peddit.peddit_api.repository;

import com.peddit.peddit_api.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    boolean existsByName(String name);


}
