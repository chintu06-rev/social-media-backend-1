package com.socialsync.repository;

import com.socialsync.model.Post;
import com.socialsync.model.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findAllByUserIdOrderByCreatedAtDesc(Long userId);
    long countByStatus(PostStatus status);
    long countByUserId(Long userId);
    long countByUserIdAndStatus(Long userId, PostStatus status);
    List<Post> findByStatusAndScheduledAtLessThanEqual(PostStatus status, LocalDateTime now);
}
