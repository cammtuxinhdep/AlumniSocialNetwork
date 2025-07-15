/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vmct.repositories;

import com.vmct.pojo.PostComments;
import java.util.List;

/**
 *
 * @author HP
 */
public interface PostCommentsRepository {
   List<PostComments> getCommentsByPostId(Integer postId);
    PostComments getCommentById(Integer commentId);
    PostComments saveComment(PostComments comment);
    void deleteComment(Integer commentId);
}
