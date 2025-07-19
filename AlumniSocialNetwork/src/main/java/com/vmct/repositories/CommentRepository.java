/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vmct.repositories;

import com.vmct.pojo.Comment;
import java.util.List;

/**
 *
 * @author Thanh Nhat
 */
public interface CommentRepository {
    Comment findById(Long id);
    boolean save(Comment comment);
    boolean delete(Long id);
    List<Comment> findByPostId(Long postId);
}
