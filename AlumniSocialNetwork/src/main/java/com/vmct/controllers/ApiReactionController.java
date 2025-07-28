package com.vmct.controllers;

import com.vmct.dto.ReactionStatsDTO;
import com.vmct.pojo.Reaction;
import com.vmct.services.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reaction")
@CrossOrigin // hoặc chỉ cho domain frontend
public class ApiReactionController {

    @Autowired
    private ReactionService reactionService;
    @PostMapping
    public ResponseEntity<?> createOrUpdateReaction(@RequestBody Reaction reaction) {
        boolean result = reactionService.save(reaction);
        if (result)
            return ResponseEntity.ok(reaction);
        return ResponseEntity.badRequest().body("Invalid data or save failed");
    }

    @DeleteMapping("/{reactionId}")
    public ResponseEntity<?> deleteReaction(@PathVariable("reactionId") Long reactionId) {
        boolean result = reactionService.delete(reactionId);
        if (result)
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reaction not found");
    }

    @GetMapping("/stats/{postId}")
    public ResponseEntity<Map<String, Integer>> getReactionStats(@PathVariable("postId") Long postId) {
        Map<String, Integer> stats = reactionService.getReactionStats(postId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/user/{userId}/post/{postId}")
    public ResponseEntity<Reaction> getUserReaction(
            @PathVariable("userId") Long userId,
            @PathVariable("postId") Long postId
    ) {
        Reaction reaction = reactionService.getUserReaction(postId, userId);
        if (reaction != null)
            return ResponseEntity.ok(reaction);
        return ResponseEntity.notFound().build();
    }
}
