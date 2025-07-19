/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vmct.services;

import com.vmct.pojo.Posts;
import java.util.List;

/**
 *
 * @author Thanh Nhat
 */
public interface PostService {
    List<Posts> getAllPosts();
    Posts createPost(Posts post);
    Posts getPostById(Long id);
    void updatePost(Posts post);
    void deletePost(Long id);
    void lockComments(Long postId, boolean lock);
}
