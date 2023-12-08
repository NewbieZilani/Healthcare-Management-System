package com.moin.CommunityPortal.repository;

import com.moin.CommunityPortal.entity.CommentEntity;
import com.moin.CommunityPortal.entity.PostEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    @Query("SELECT c FROM CommentEntity c WHERE c.post.id = :postId")
    List<CommentEntity> findByPostId(@Param("postId") Long postId);
}
