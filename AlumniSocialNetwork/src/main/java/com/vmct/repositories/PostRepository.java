/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vmct.repositories;

import com.vmct.pojo.Post;
import java.util.List;

/**
 *
 * @author Thanh Nhat
 */
public interface PostRepository {
    Post getPostById(Long id);
    boolean addOrUpdatePost(Post post);
    boolean deletePost(Long id);
    List<Post> getPostByUserId(Long userId);
    List<Post> getAllPost();
}
