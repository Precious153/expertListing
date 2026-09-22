package com.example.expertListing.repository;

import com.example.expertListing.entity.Comment;
import com.example.expertListing.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByCreatedAtAsc(Post post);
    long countByPost(Post post);
}
