package com.vmct.controllers;

import com.vmct.pojo.Post;
import com.vmct.pojo.User;
import com.vmct.services.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/post")

public class PostController {

    @Autowired
    private PostService postService;

    // GET: Hiển thị danh sách post + form tạo mới
    @GetMapping
    public String listPosts(Model model) {
        model.addAttribute("post", new Post()); // Form tạo mới
        model.addAttribute("posts", postService.getAllPostSummaries()); // DTO danh sách
        return "post";
    }

    // POST: Tạo post mới
    @PostMapping
    public String createPost(@ModelAttribute("post") Post post, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        postService.createPost(post, currentUser);
        return "redirect:/post";
    }

    // GET: Hiển thị form cập nhật post
    @GetMapping("/{id}")
    public String editPostForm(@PathVariable("id") Long id, Model model) {
        Post existingPost = postService.getPostById(id);
        if (existingPost == null) {
            return "redirect:/post";
        }
        model.addAttribute("post", existingPost);
        model.addAttribute("posts", postService.getAllPostSummaries());
        return "post";
    }

    // POST: Cập nhật post
    @PostMapping("/{id}")
    public String updatePost(@PathVariable("id") Long id, @ModelAttribute("post") Post post) {
        post.setId(id);
        postService.updatePost(post);
        return "redirect:/post";
    }

    // GET: Xoá post
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return "redirect:/post";
    }

    // POST: Khoá/mở comment
    @PostMapping("/{id}/lock-comments")
    public String toggleLockComments(@PathVariable("id") Long id, @RequestParam("lock") boolean lock) {
        postService.lockComments(id, lock);
        return "redirect:/post";
    }
}