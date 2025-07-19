/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vmct.services;

import com.vmct.pojo.Post;
import java.util.List;

/**
 *
 * @author Thanh Nhat
 */
public interface PostService {
    List<Post> getAllPost();
    Post createPost(Post post);
    Post getPostById(Long id);
    void updatePost(Post post);
    void deletePost(Long id);
    void lockComments(Long postId, boolean lock);
}
