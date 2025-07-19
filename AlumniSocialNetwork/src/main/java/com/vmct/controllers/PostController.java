package com.vmct.controllers;

import com.vmct.pojo.Posts;
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
    public String listPosts(Model model) {
        model.addAttribute("post", new Posts());
        model.addAttribute("posts", postService.getAllPosts());
        return "posts";
    }

    @PostMapping("/posts")
    public String addPost(@ModelAttribute("post") Posts post) {
        postService.createPost(post);
        return "redirect:/posts";
    }

    @GetMapping("/posts/{postId}")
    public String updatePostForm(Model model, @PathVariable("postId") Long id) {
        Posts post = postService.getPostById(id);
        model.addAttribute("post", post != null ? post : new Posts());
        model.addAttribute("posts", postService.getAllPosts());
        return "posts";
    }

    @PostMapping("/posts/{postId}")
    public String updatePost(@PathVariable("postId") Long id, @ModelAttribute("post") Posts post) {
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
