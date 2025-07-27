package com.vmct.services;

import com.vmct.dto.CommentDTO;
import com.vmct.pojo.Comment;

import java.util.List;

public interface CommentService {

    Comment findById(Long commentId);

    boolean save(Comment comment);

    boolean delete(Long commentId);

    int countByPostId(Long postId);

    List<CommentDTO> getRootCommentsWithFirstLevelReplies(Long postId); // ✅ load gốc + 1 cấp

    List<CommentDTO> getReplies(Long parentId); // ✅ lazy load replies
}
