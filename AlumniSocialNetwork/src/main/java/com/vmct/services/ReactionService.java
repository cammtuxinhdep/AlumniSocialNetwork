package com.vmct.services;

import com.vmct.dto.ReactionStatsDTO;
import com.vmct.pojo.Reaction;
import java.util.List;
import java.util.Map;

public interface ReactionService {
    Reaction findById(Long reactionId);
  boolean saveOrUpdateReaction(Reaction reaction);
    boolean delete(Long reactionId);
    List<Reaction> findByPostId(Long postId);
    int countByPostId(Long postId);
    int countByPostIdAndType(Long postId, String type);
 Map<String, Integer> getReactionStats(Long postId);
    Reaction getUserReaction(Long postId, Long userId);
}