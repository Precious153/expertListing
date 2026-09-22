package com.example.expertListing.repository;

import com.example.expertListing.entity.Like;
import com.example.expertListing.entity.Post;
import com.example.expertListing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser(Post post, User user);
    long countByPost(Post post);
}
