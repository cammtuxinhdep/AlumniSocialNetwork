package com.vmct.repositories;

import com.vmct.pojo.Posts;
import java.util.List;
import java.util.Map;

public interface PostRepository {
    Posts getPostById(Long id);
    boolean addOrUpdatePost(Posts post);
    boolean deletePost(Long id);
<<<<<<< Updated upstream
    List<Posts> getPostsByUserId(Long userId);
    List<Posts> getAllPosts();
=======
    List<Post> getPostByUserId(Long userId);
    List<Post> getAllPosts(Map<String, String> params);
>>>>>>> Stashed changes
}
