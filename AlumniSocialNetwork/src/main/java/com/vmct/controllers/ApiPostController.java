/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.controllers;

/**
 *
 * @author Thanh Nhat
 */
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
public class ApiPostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<PostSummaryDTO>> listPosts() {
        List<PostSummaryDTO> posts = postService.getAllPostSummaries();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long postId, HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        PostDTO postDTO = postService.getPostDTOById(postId, currentUser);
        if (postDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post, HttpServletRequest request) {
        User currentUser = (User) request.getAttribute("currentUser");
        Post created = postService.createPost(post, currentUser);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId, @RequestBody Post post) {
        post.setId(postId);
        postService.updatePost(post);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{postId}/lock-comments")
    public ResponseEntity<Void> lockComments(@PathVariable Long postId, @RequestParam boolean lock) {
        postService.lockComments(postId, lock);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
