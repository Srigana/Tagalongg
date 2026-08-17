package com.tagalong.repository;

import com.tagalong.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByDestinationIgnoreCaseAndStatus(String destination, Post.PostStatus status);
}
