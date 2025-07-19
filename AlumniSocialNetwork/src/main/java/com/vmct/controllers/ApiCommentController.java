package com.vmct.api;

import com.vmct.pojo.Comments;
import com.vmct.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class ApiCommentController {

    @Autowired
    private CommentService commentService;

    // Lấy danh sách bình luận theo bài viết
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comments>> getCommentsByPost(@PathVariable("postId") Long postId) {
        List<Comments> comments = commentService.findByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // Lấy 1 bình luận theo id
    @GetMapping("/{id}")
    public ResponseEntity<Comments> getCommentById(@PathVariable("id") Long id) {
        Comments comment = commentService.findById(id);
        if (comment != null)
            return ResponseEntity.ok(comment);
        else
            return ResponseEntity.notFound().build();
    }

    // Thêm mới bình luận
    @PostMapping("/")
    public ResponseEntity<?> createComment(@RequestBody Comments comment) {
        boolean result = commentService.save(comment);
        if (result)
            return ResponseEntity.ok(comment);
        else
            return ResponseEntity.badRequest().body("Failed to save comment");
    }

    // Cập nhật bình luận
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable("id") Long id, @RequestBody Comments updatedComment) {
        Comments existing = commentService.findById(id);
        if (existing == null)
            return ResponseEntity.notFound().build();

        updatedComment.setId(id);
        boolean result = commentService.save(updatedComment);
        if (result)
            return ResponseEntity.ok(updatedComment);
        else
            return ResponseEntity.badRequest().body("Update failed");
    }

    // Xoá bình luận
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id) {
        Comments existing = commentService.findById(id);
        if (existing == null)
            return ResponseEntity.notFound().build();

        boolean result = commentService.delete(id);
        if (result)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(500).body("Delete failed");
    }
}
