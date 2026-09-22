package com.example.expertListing.service;

import com.example.expertListing.dto.CommentRequest;
import com.example.expertListing.dto.CommentResponse;
import com.example.expertListing.dto.PostRequest;
import com.example.expertListing.dto.PostResponse;
import com.example.expertListing.entity.Comment;
import com.example.expertListing.entity.Like;
import com.example.expertListing.entity.Post;
import com.example.expertListing.entity.User;
import com.example.expertListing.repository.CommentRepository;
import com.example.expertListing.repository.LikeRepository;
import com.example.expertListing.repository.PostRepository;
import com.example.expertListing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.example.expertListing.dto.PaginatedResponse;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public PaginatedResponse<PostResponse> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        
        Page<PostResponse> responsePage = postPage.map(this::mapToPostResponse);
        return PaginatedResponse.fromPage(responsePage);
    }

    @Transactional
    public PostResponse createPost(Long userId, PostRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = Post.builder()
                .user(user)
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .location(request.getLocation())
                .transactionType(request.getTransactionType())
                .build();

        post = postRepository.save(post);
        return mapToPostResponse(post);
    }

    @Transactional
    public void toggleLike(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        Optional<Like> existingLike = likeRepository.findByPostAndUser(post, user);
        
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
        } else {
            Like like = Like.builder()
                    .post(post)
                    .user(user)
                    .build();
            likeRepository.save(like);
        }
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        return commentRepository.findByPostOrderByCreatedAtAsc(post).stream()
                .map(this::mapToCommentResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse addComment(Long userId, Long postId, CommentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(request.getContent())
                .build();

        comment = commentRepository.save(comment);
        return mapToCommentResponse(comment);
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
                
        if (!post.getUser().getId().equals(userId)) {
            throw new org.springframework.security.access.AccessDeniedException("You are not authorized to delete this post");
        }
        
        // Delete associated comments and likes first
        commentRepository.deleteByPost(post);
        likeRepository.deleteByPost(post);
        
        postRepository.delete(post);
    }

    private PostResponse mapToPostResponse(Post post) {
        long commentCount = commentRepository.countByPost(post);
        long likeCount = likeRepository.countByPost(post);

        return PostResponse.builder()
                .id(post.getId())
                .userId(post.getUser().getId())
                .authorName(post.getUser().getFirstName() + " " + post.getUser().getLastName())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .location(post.getLocation())
                .transactionType(post.getTransactionType())
                .createdAt(post.getCreatedAt())
                .commentCount((int) commentCount)
                .likeCount((int) likeCount)
                .build();
    }

    private CommentResponse mapToCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .authorName(comment.getUser().getFirstName() + " " + comment.getUser().getLastName())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
