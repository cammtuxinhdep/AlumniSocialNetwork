package com.vmct.services.impl;

import com.vmct.pojo.Comment;
import com.vmct.repositories.CommentRepository;
import com.vmct.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author Thanh Nhat
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepo;

    @Override
    public Comment findById(Long cId) {
        try {
            return commentRepo.findById(cId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean save(Comment c) {
        try {
            return commentRepo.save(c);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Long cId) {
        try {
            return commentRepo.delete(cId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Comment> findByPostId(Long pId) {
        try {
            return commentRepo.findByPostId(pId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}