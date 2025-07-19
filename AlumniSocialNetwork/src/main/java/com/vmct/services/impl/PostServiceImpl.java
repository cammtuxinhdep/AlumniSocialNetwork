package com.vmct.services.impl;

import com.vmct.pojo.Comments;
import com.vmct.pojo.Posts;
import com.vmct.pojo.Users;
import com.vmct.repositories.CommentRepository;
import com.vmct.repositories.PostRepository;
import com.vmct.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Thanh Nhat
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepo;

    @Override
    public Posts createPost(Posts p) {
        try {
            p.setCreatedAt(new Date());
            p.setIsCommentLocked(false);
            if (postRepo.addOrUpdatePost(p)) {
                return p;
            }
            return null; // Trả về null nếu thêm thất bại
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Posts getPostById(Long id) {
        try {
            return postRepo.getPostById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updatePost(Posts p) {
        try {
            p.setUpdatedAt(new Date());
            postRepo.addOrUpdatePost(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePost(Long id) {
        try {
            postRepo.deletePost(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void lockComments(Long pId, boolean lock) {
        try {
            Posts p = getPostById(pId);
            if (p != null) {
                p.setIsCommentLocked(lock);
                updatePost(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Posts> getAllPosts() {
        try {
            return postRepo.getAllPosts();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
