/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vmct.repositories;

import com.vmct.pojo.PostItems;
import java.util.List;

/**
 *
 * @author HP
 */
public interface PostItemsRepository {
   List<PostItems> getPosts();
    PostItems getPostById(Integer postId);
    PostItems savePost(PostItems post);
    void deletePost(Integer postId);
}
