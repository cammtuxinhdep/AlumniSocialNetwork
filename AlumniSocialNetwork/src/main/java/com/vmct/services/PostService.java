
package com.vmct.services;

import com.vmct.dto.PostDTO;
import com.vmct.dto.PostSummaryDTO;
import com.vmct.pojo.Post;
import com.vmct.pojo.User;

import java.util.List;
import java.util.Map;

public interface PostService {

    List<PostSummaryDTO> getAllPostSummaries();
    List<PostSummaryDTO> getAllPostSummaries(Map<String, String> params);

    Post createPost(Post post, User creator);

    Post getPostById(Long id);

    PostDTO getPostDTOById(Long id, User currentUser);

    void updatePost(Post post);

    void deletePost(Long id);

    void lockComments(Long postId, boolean locked);
    List<Post> getUserPosts(Long id);
}