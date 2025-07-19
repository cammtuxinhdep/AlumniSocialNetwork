package com.vmct.services.impl;

import com.vmct.pojo.Post;
import com.vmct.pojo.User;
import com.vmct.repositories.PostRepository;
import com.vmct.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Post createPost(Post p) {
        try {
            p.setCreatedAt(new Date());
            p.setIsCommentLocked(false);

            // GÁN MẪU USER để tránh lỗi null (chỉ dùng khi test)
            User u = new User();
            u.setId(1L); // Giả sử user có ID = 1 tồn tại trong DB
            p.setUserId(u);

            if (postRepo.addOrUpdatePost(p)) {
                return p;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Post getPostById(Long id) {
        try {
            return postRepo.getPostById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updatePost(Post p) {
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
            Post p = getPostById(pId);
            if (p != null) {
                p.setIsCommentLocked(lock);
                updatePost(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAllPost() {
        try {
            return postRepo.getAllPost();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
