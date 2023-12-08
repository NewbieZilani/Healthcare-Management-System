package com.moin.CommunityPortal.repository;

import com.moin.CommunityPortal.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity,Long> {
}
