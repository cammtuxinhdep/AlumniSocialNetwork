package com.vmct.services;

import com.vmct.pojo.Comment;
import java.util.List;

/**
 *
 * @author Thanh Nhat
 */
public interface CommentService {
    Comment findById(Long cId);
    boolean save(Comment c);
    boolean delete(Long cId);
    List<Comment> findByPostId(Long pId);
}