package com.vmct.repositories;

import com.vmct.pojo.Reaction;
import java.util.List;
import java.util.Map;

public interface ReactionRepository {
    Reaction getReactionById(Long id);
    List<Reaction> getReactionsByPostId(Long postId);
    int countByPostId(Long postId);
    int countByPostIdAndType(Long postId, String type);
    boolean deleteReaction(Long id);
    boolean saveOrUpdate(Reaction reaction);
    Map<String, Integer> getReactionStats(Long postId); // dùng Map thay vì DTO nếu bạn xử lý đơn giản
    Reaction getUserReaction(Long postId, Long userId);
}
