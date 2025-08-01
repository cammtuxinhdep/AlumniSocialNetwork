package com.vmct.services.impl;
import com.vmct.dto.PostDTO;
import com.vmct.dto.PostSummaryDTO;
import com.vmct.pojo.Post;
import com.vmct.pojo.Reaction;
import com.vmct.pojo.User;
import com.vmct.repositories.PostRepository;
import com.vmct.services.CommentService;
import com.vmct.services.PostService;
import com.vmct.services.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private CommentService commentService;

    @Override
    public Post createPost(Post p, User currentUser) {
        try {
            p.setCreatedAt(new Date());
            p.setIsCommentLocked(false);
            p.setUserId(currentUser);

            if (postRepo.addOrUpdatePost(p)) {
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    // ✅ Đã bỏ getAllPost() — không còn sử dụng
    // Nếu cần API public để get all post, có thể dùng getAllPostSummaries hoặc viết mới với param

    @Override
    public List<PostSummaryDTO> getAllPostSummaries() {
        try {
            Map<String, String> params = new HashMap<>(); // có thể truyền thêm "kw", "userId", "page", v.v.
            List<Post> posts = postRepo.getAllPosts(params);

            return posts.stream()
                    .map(post -> new PostSummaryDTO(
                            post,
                            commentService.countByPostId(post.getId()),
                            reactionService.getReactionStats(post.getId())
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

   @Override
public PostDTO getPostDTOById(Long id, User currentUser) {
    Post post = postRepo.getPostById(id);
    if (post == null) {
        return null;
    }

    Reaction reaction = reactionService.getUserReaction(post.getId(), currentUser.getId());
    String userReactionType = (reaction != null) ? reaction.getType() : null;

    return new PostDTO(
            post,
            commentService.getRootCommentsWithFirstLevelReplies(post.getId()), // ✅ CHỈ LẤY GỐC + 1 CẤP
            reactionService.getReactionStats(post.getId()),
            userReactionType,
            commentService.countByPostId(post.getId())
    );
}
}