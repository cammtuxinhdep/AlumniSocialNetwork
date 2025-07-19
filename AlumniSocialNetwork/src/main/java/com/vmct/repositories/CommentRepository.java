/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vmct.repositories;

import com.vmct.pojo.Comments;
import java.util.List;

/**
 *
 * @author Thanh Nhat
 */
public interface CommentRepository {
    Comments findById(Long id);
    boolean save(Comments comment);
    boolean delete(Long id);
    List<Comments> findByPostId(Long postId);
}
