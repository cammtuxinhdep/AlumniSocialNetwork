/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.controllers;

/**
 *
 * @author Thanh Nhat
 */
@Controller
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String listPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "posts/list";
    }

    @GetMapping("/add")
    public String addPostForm(Model model) {
        model.addAttribute("post", new Posts());
        return "posts/add";
    }

    @PostMapping("/add")
    public String addPost(@ModelAttribute("post") @Valid Posts post, BindingResult rs) {
        if (!rs.hasErrors()) {
            postService.addPost(post);
            return "redirect:/posts/";
        }
        return "posts/add";
    }

    @GetMapping("/edit/{id}")
    public String editPostForm(Model model, @PathVariable Long id) {
        model.addAttribute("post", postService.getPostById(id));
        return "posts/edit";
    }

    @PostMapping("/edit/{id}")
    public String editPost(@ModelAttribute("post") @Valid Posts post, BindingResult rs) {
        if (!rs.hasErrors()) {
            postService.updatePost(post);
            return "redirect:/posts/";
        }
        return "posts/edit";
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/posts/";
    }
}
