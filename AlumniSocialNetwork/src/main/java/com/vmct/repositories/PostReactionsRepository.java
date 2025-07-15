/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vmct.repositories;

import com.vmct.pojo.PostReactions;
import java.util.List;

/**
 *
 * @author HP
 */
public interface PostReactionsRepository {
    List<PostReactions> getReactionsByPostId(Integer postId);
    PostReactions getReactionById(Integer reactionId);
    PostReactions saveReaction(PostReactions reaction);
    void deleteReaction(Integer reactionId);
}
