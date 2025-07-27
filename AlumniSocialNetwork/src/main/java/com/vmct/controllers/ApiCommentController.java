/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.vmct.controllers;

import com.vmct.dto.CommentDTO;
import com.vmct.pojo.Comment;
import com.vmct.services.CommentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/comment")
@CrossOrigin
public class ApiCommentController {

    @Autowired
    private CommentService commentService;

    // ✅ GET: Lấy các comment gốc và reply cấp 1 của post
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsForPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(commentService.getRootCommentsWithFirstLevelReplies(postId));
    }

    // ✅ GET: Lấy reply cấp tiếp theo của một comment cụ thể (lazy load)
    @GetMapping("/replies/{parentId}")
    public ResponseEntity<List<CommentDTO>> getReplies(@PathVariable("parentId") Long parentId) {
        return ResponseEntity.ok(commentService.getReplies(parentId));
    }

    // ✅ GET: Lấy chi tiết comment theo ID
    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable("id") Long id) {
        Comment comment = commentService.findById(id);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new CommentDTO(comment));
    }

    // ✅ POST: Tạo mới comment hoặc reply
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody Comment comment) {
        boolean saved = commentService.save(comment);
        if (saved) {
            return ResponseEntity.ok(new CommentDTO(comment));
        }
        return ResponseEntity.badRequest().body("Failed to create comment");
    }

    // ✅ PUT: Cập nhật comment
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable("id") Long id, @RequestBody Comment comment) {
        Comment existing = commentService.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        comment.setId(id); // đảm bảo dùng đúng ID
        boolean updated = commentService.save(comment);
        if (updated) {
            return ResponseEntity.ok(new CommentDTO(comment));
        }
        return ResponseEntity.badRequest().body("Failed to update comment");
    }

    // ✅ DELETE: Xoá comment
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id) {
        Comment existing = commentService.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        boolean deleted = commentService.delete(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).body("Not authorized to delete this comment");
    }
}