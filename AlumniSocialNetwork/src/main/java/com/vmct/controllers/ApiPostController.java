package com.vmct.controllers;

import com.vmct.dto.PostDTO;
import com.vmct.dto.PostSummaryDTO;
import com.vmct.pojo.Post;
import com.vmct.pojo.User;
import com.vmct.services.PostService;
import com.vmct.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/secure/post")
@CrossOrigin
public class ApiPostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

@GetMapping
public ResponseEntity<List<PostSummaryDTO>> listPosts(
        @RequestParam Map<String, String> params,
        Principal principal) {
    User currentUser = userService.getUserByUsername(principal.getName());
    if (currentUser == null) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    List<PostSummaryDTO> posts = postService.getAllPostSummaries(params);
    return new ResponseEntity<>(posts, HttpStatus.OK);
}


    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long postId, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        PostDTO postDTO = postService.getPostDTOById(postId, currentUser);
        if (postDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Post created = postService.createPost(post, currentUser);
        if (created == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
    public ResponseEntity<Void> lockComments(
            @PathVariable("postId") Long postId,
            @RequestParam(name = "lock") boolean lock,
            Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Post post = postService.getPostById(postId);
        if (post == null || !post.getUserId().getId().equals(currentUser.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        postService.lockComments(postId, lock);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}