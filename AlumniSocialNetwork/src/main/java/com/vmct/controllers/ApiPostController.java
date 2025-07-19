package com.vmct.controllers;

import com.vmct.pojo.Posts;
import com.vmct.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiPostController {
    @Autowired
    private PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<List<Posts>> listPosts() {
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Posts> getPost(@PathVariable("postId") Long id) {
        Posts post = postService.getPostById(id);
        if (post == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping("/posts")
    public ResponseEntity<Posts> createPost(@RequestBody Posts post) {
        Posts created = postService.createPost(post);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable("postId") Long id, @RequestBody Posts post) {
        post.setId(id);
        postService.updatePost(post);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/posts/{postId}/lock-comments")
    public ResponseEntity<Void> lockComments(@PathVariable("postId") Long postId, @RequestParam("lock") boolean lock) {
        postService.lockComments(postId, lock);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
