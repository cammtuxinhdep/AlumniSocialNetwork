package com.vmct.repositories;

import com.vmct.pojo.Post;
import java.util.List;
import java.util.Map;

public interface PostRepository {
    Post getPostById(Long id);
    boolean addOrUpdatePost(Post post);
    boolean deletePost(Long id);

    List<Post> getPostByUserId(Long userId);
    List<Post> getAllPosts(Map<String, String> params);

}