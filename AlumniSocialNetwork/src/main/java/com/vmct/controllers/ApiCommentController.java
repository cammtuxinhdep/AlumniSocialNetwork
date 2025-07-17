/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Thanh Nhat
 */
@RestController
@RequestMapping("/api/comments")
@CrossOrigin
public class ApiCommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comments>> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @PostMapping("/add")
    public ResponseEntity<Comments> addComment(@RequestBody Comments comment, HttpSession session) {
        Users currentUser = (Users) session.getAttribute("currentUser");
        comment.setUserId(currentUser);
        return ResponseEntity.ok(commentService.addComment(comment));
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id, HttpSession session) {
        Users currentUser = (Users) session.getAttribute("currentUser");
        Comments c = commentService.getCommentById(id);
        if (c.getUserId().getId().equals(currentUser.getId()))
            commentService.deleteComment(id);
    }
}
