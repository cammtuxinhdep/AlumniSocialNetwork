/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vmct.repositories;

import com.vmct.pojo.Posts;
import java.util.List;

/**
 *
 * @author Thanh Nhat
 */
public interface PostRepository {
    Posts getPostById(Long id);
    boolean addOrUpdatePost(Posts post);
    boolean deletePost(Long id);
    List<Posts> getPostsByUserId(Long userId);
    List<Posts> getAllPosts();
}
