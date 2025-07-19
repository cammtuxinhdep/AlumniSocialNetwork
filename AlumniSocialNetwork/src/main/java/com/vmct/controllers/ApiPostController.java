package com.vmct.controllers;

import com.vmct.dto.PostDTO;
import com.vmct.dto.PostSummaryDTO;
import com.vmct.pojo.Post;
import com.vmct.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiPostController {

    @Autowired
    private PostService postService;

    // Lấy danh sách tất cả bài đăng (dạng DTO)
  @GetMapping("/posts")
    public ResponseEntity<List<PostSummaryDTO>> listPost() {
        List<Post> posts = postService.getAllPost();
        List<PostSummaryDTO> postDTOs = posts.stream()
                .map(PostSummaryDTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }

    // Lấy chi tiết một bài đăng theo ID (dạng DTO)
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable("postId") Long id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new PostDTO(post), HttpStatus.OK);
    }

// Tạo một bài đăng mới (vẫn nhận và trả về entity gốc)
    @PostMapping("/posts")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post created = postService.createPost(post);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Cập nhật một bài đăng
    @PutMapping("/posts/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable("postId") Long id, @RequestBody Post post) {
        post.setId(id);
        postService.updatePost(post);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Xoá một bài đăng
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Khoá hoặc mở comment cho bài đăng
    @PostMapping("/posts/{postId}/lock-comments")
    public ResponseEntity<Void> lockComments(@PathVariable("postId") Long postId, @RequestParam("lock") boolean lock) {
        postService.lockComments(postId, lock);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
