package com.vmct.services.impl;

import com.vmct.dto.CommentDTO;
import com.vmct.pojo.Comment;
import com.vmct.pojo.User;
import com.vmct.repositories.CommentRepository;
import com.vmct.services.CommentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    @Override
    public boolean save(Comment comment) {
        try {
            if (comment.getCreatedAt() == null) {
                comment.setCreatedAt(new Date());
            }
            commentRepository.save(comment);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean delete(Long commentId) {
        try {
            commentRepository.deleteById(commentId);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public int countByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }

    @Override
    public List<CommentDTO> getRootCommentsWithFirstLevelReplies(Long postId) {
        List<Comment> allComments = commentRepository.findByPostId(postId);

        // Tách root và các replies
        Map<Long, List<Comment>> repliesByParentId = new HashMap<>();
        List<Comment> rootComments = new ArrayList<>();

        for (Comment c : allComments) {
            if (c.getParentId() == null) {
                rootComments.add(c);
            } else {
                Long parentId = c.getParentId().getId();
                repliesByParentId.computeIfAbsent(parentId, k -> new ArrayList<>()).add(c);
            }
        }

        // Duyệt root comment và gắn replies cấp 1
        return rootComments.stream()
                .map(root -> {
                    CommentDTO rootDTO = new CommentDTO(root);
                    List<Comment> firstLevelReplies = repliesByParentId.get(root.getId());
                    if (firstLevelReplies != null) {
                        List<CommentDTO> replyDTOs = firstLevelReplies.stream()
                                .map(CommentDTO::new)
                                .collect(Collectors.toList());
                        rootDTO.setReplies(replyDTOs);
                    }
                    return rootDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> getReplies(Long parentId) {
        List<Comment> replies = commentRepository.findByParentId(parentId);
        return replies.stream().map(CommentDTO::new).collect(Collectors.toList());
    }
}

