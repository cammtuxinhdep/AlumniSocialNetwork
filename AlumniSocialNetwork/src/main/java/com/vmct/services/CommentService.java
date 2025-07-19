package com.vmct.services;

import com.vmct.pojo.Comments;
import java.util.List;

/**
 *
 * @author Thanh Nhat
 */
public interface CommentService {
    Comments findById(Long cId);
    boolean save(Comments c);
    boolean delete(Long cId);
    List<Comments> findByPostId(Long pId);
}