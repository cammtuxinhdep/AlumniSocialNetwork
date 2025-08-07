package com.vmct.services.impl;

import com.vmct.pojo.Reaction;
import com.vmct.repositories.ReactionRepository;
import com.vmct.services.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ReactionServiceImpl implements ReactionService {

    @Autowired
    private ReactionRepository reactionRepo;

    @Override
    public Reaction findById(Long reactionId) {
        if (reactionId == null || reactionId <= 0) {
            return null;
        }
        return reactionRepo.getReactionById(reactionId);
    }

@Override
public boolean saveOrUpdateReaction(Reaction reaction) {
    if (reaction == null || reaction.getPostId() == null || reaction.getPostId().getId() == null ||
        reaction.getUserId() == null || reaction.getUserId().getId() == null ||
        reaction.getType() == null || reaction.getType().isEmpty()) {
        return false;
    }

    Reaction existing = reactionRepo.getUserReaction(
        reaction.getPostId().getId(),
        reaction.getUserId().getId()
    );

    if (existing != null) {
        if (existing.getType().equals(reaction.getType())) {
            return reactionRepo.deleteReaction(existing.getId());
        } else {
            existing.setType(reaction.getType());
            return reactionRepo.saveOrUpdate(existing);
        }
    } else {
        return reactionRepo.saveOrUpdate(reaction);
    }
}


    @Override
    public boolean delete(Long reactionId) {
        if (reactionId == null || reactionId <= 0) {
            return false;
        }
        return reactionRepo.deleteReaction(reactionId);
    }

    @Override
    public List<Reaction> findByPostId(Long postId) {
        if (postId == null || postId <= 0) {
            return Collections.emptyList();
        }
        List<Reaction> reactions = reactionRepo.getReactionsByPostId(postId);
        return reactions != null ? reactions : Collections.emptyList();
    }

    @Override
    public int countByPostId(Long postId) {
        if (postId == null || postId <= 0) {
            return 0;
        }
        return reactionRepo.countByPostId(postId);
    }

    @Override
    public int countByPostIdAndType(Long postId, String type) {
        if (postId == null || postId <= 0 || type == null || type.isEmpty()) {
            return 0;
        }
        return reactionRepo.countByPostIdAndType(postId, type);
    }

 @Override
public Map<String, Integer> getReactionStats(Long postId) {
    if (postId == null || postId <= 0) return Collections.emptyMap();
    return reactionRepo.getReactionStats(postId);
}


    @Override
    public Reaction getUserReaction(Long postId, Long userId) {
        if (postId == null || postId <= 0 || userId == null || userId <= 0) {
            return null;
        }
        return reactionRepo.getUserReaction(postId, userId);
    }
}