package com.vmct.controllers;

import com.vmct.services.CommentService;
import com.vmct.pojo.Comment;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/post/{postId}")
    public String listCommentsByPost(@PathVariable("postId") Long postId, Model model) {
        List<Comment> comments = commentService.findByPostId(postId);
        model.addAttribute("comments", comments);
        return "comments"; // tên file .html view
    }

    @GetMapping("/edit/{id}")
    public String editComment(@PathVariable("id") Long id, Model model) {
        Comment comment = commentService.findById(id);
        model.addAttribute("comment", comment);
        return "comment-edit";
    }

    @PostMapping("/edit")
    public String updateComment(@ModelAttribute("comment") @Valid Comment comment) {
        commentService.save(comment);
        return "redirect:/comments/post/" + comment.getPostId().getId();  
    }

    @GetMapping("/delete/{id}")
    public String deleteComment(@PathVariable("id") Long id) {
        Comment c = commentService.findById(id);
        if (c != null)
            commentService.delete(id);
        return "redirect:/comments/post/" + c.getPostId().getId();  
    }
}
