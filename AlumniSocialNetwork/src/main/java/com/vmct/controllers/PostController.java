package com.vmct.controllers;

import com.vmct.pojo.Post;
import com.vmct.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/posts")
    public String listPost(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("posts", postService.getAllPost());
        return "posts";
    }

    @PostMapping("/posts")
    public String addPost(@ModelAttribute("post") Post post) {
        postService.createPost(post);
        return "redirect:/posts";
    }

    @GetMapping("/posts/{postId}")
    public String updatePostForm(Model model, @PathVariable("postId") Long id) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post != null ? post : new Post());
        model.addAttribute("posts", postService.getAllPost());
        return "posts";
    }

    @PostMapping("/posts/{postId}")
    public String updatePost(@PathVariable("postId") Long id, @ModelAttribute("post") Post post) {
        post.setId(id);
        postService.updatePost(post);
        return "redirect:/posts";
    }

    @GetMapping("/posts/delete/{postId}")
    public String deletePost(@PathVariable("postId") Long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }

    @PostMapping("/posts/{postId}/lock-comments")
    public String lockComments(@PathVariable("postId") Long postId, @RequestParam("lock") boolean lock) {
        postService.lockComments(postId, lock);
        return "redirect:/posts";
    }
}
