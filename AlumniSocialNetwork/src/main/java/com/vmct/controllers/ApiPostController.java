/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.controllers;

<<<<<<< Updated upstream
/**
 *
 * @author Thanh Nhat
 */
@RestController
@RequestMapping("/api/posts")
=======
import com.vmct.dto.PostDTO;
import com.vmct.dto.PostSummaryDTO;
import com.vmct.pojo.Post;
import com.vmct.pojo.User;
import com.vmct.services.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@CrossOrigin
>>>>>>> Stashed changes
public class ApiPostController {
     @Autowired
    private PostService postService;

<<<<<<< Updated upstream
    @GetMapping("/")
    public ResponseEntity<List<Posts>> getAll() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Posts> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
=======
    // GET /api/post - danh sách bài viết (không chứa bình luận)
    @GetMapping
    public ResponseEntity<List<PostSummaryDTO>> listPosts() {
        List<PostSummaryDTO> posts = postService.getAllPostSummaries();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // GET /api/post/{id} - chi tiết bài viết (gồm bình luận, reactions)
    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long postId, HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser"); // giả định được set ở Interceptor/Auth
        PostDTO postDTO = postService.getPostDTOById(postId, currentUser);
        if (postDTO == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    // POST /api/post - tạo bài đăng mới
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post, HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        Post created = postService.createPost(post, currentUser);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // PUT /api/post/{id} - cập nhật nội dung bài viết
    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId, @RequestBody Post post) {
        post.setId(postId);
        postService.updatePost(post);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // DELETE /api/post/{id} - xoá bài viết
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // POST /api/post/{id}/lock-comments?lock=true|false
    @PostMapping("/{postId}/lock-comments")
    public ResponseEntity<Void> lockComments(@PathVariable Long postId, @RequestParam boolean lock) {
        postService.lockComments(postId, lock);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
>>>>>>> Stashed changes
    }
}
