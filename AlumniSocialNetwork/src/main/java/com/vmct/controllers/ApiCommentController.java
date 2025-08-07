/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.controllers;

import com.vmct.dto.CommentDTO;
import com.vmct.pojo.Comment;
import com.vmct.pojo.Post;
import com.vmct.pojo.User;
import com.vmct.services.CommentService;
import com.vmct.services.PostService;
import com.vmct.services.UserService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Thanh Nhat
 */
@RestController
@RequestMapping("/api/secure/comment")
@CrossOrigin
public class ApiCommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsForPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(commentService.getRootCommentsWithFirstLevelReplies(postId));
    }

    @GetMapping("/replies/{parentId}")
    public ResponseEntity<List<CommentDTO>> getReplies(@PathVariable("parentId") Long parentId) {
        return ResponseEntity.ok(commentService.getReplies(parentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable("id") Long id) {
        Comment comment = commentService.findById(id);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new CommentDTO(comment));
    }

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody Comment comment, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());

        Post post = postService.getPostById(comment.getPostId().getId());
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bài viết không tồn tại");
        }

        if (Boolean.TRUE.equals(post.getIsCommentLocked())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bài viết đã bị khoá bình luận");
        }

        comment.setPostId(post);
        comment.setUserId(currentUser);

        boolean saved = commentService.save(comment);
        return saved ? ResponseEntity.ok(new CommentDTO(comment))
                : ResponseEntity.badRequest().body("Failed to create comment");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable("id") Long id,
            @RequestBody Comment comment,
            Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        Comment existing = commentService.findById(id);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        if (!existing.getUserId().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Bạn không có quyền sửa bình luận này");
        }

        comment.setId(id);
        comment.setUserId(currentUser);
        comment.setPostId(existing.getPostId());

        boolean updated = commentService.save(comment);
        return updated
                ? ResponseEntity.ok(new CommentDTO(comment))
                : ResponseEntity.badRequest().body("Failed to update comment");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        Comment existing = commentService.findById(id);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        boolean isOwner = existing.getUserId().getId().equals(currentUser.getId());
        boolean isPostOwner = existing.getPostId().getUserId().getId().equals(currentUser.getId());

        if (!isOwner && !isPostOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền xoá bình luận này");
        }

        boolean deleted = commentService.delete(id);
        return deleted ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xoá thất bại");
    }

}
