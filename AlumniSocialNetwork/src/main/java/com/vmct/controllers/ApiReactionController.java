package com.vmct.controllers;

import com.vmct.pojo.Reaction;
import com.vmct.pojo.User;
import com.vmct.services.ReactionService;
import com.vmct.services.UserService;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secure/reaction")
@CrossOrigin
public class ApiReactionController {

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createOrUpdateReaction(@RequestBody Reaction reaction, Principal principal) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        User currentUser = userService.getUserByUsername(principal.getName());
        if (currentUser == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        reaction.setUserId(currentUser); 
        boolean result = reactionService.saveOrUpdateReaction(reaction);
        if (result)
            return ResponseEntity.ok(reaction);
        return ResponseEntity.badRequest().body("Invalid data or save failed");
    }

    @DeleteMapping("/{reactionId}")
    public ResponseEntity<?> deleteReaction(@PathVariable("reactionId") Long reactionId) {
        boolean result = reactionService.delete(reactionId);
        if (result) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reaction not found");
    }

    @GetMapping("/stats/{postId}")
    public ResponseEntity<Map<String, Integer>> getReactionStats(@PathVariable("postId") Long postId) {
        Map<String, Integer> stats = reactionService.getReactionStats(postId);
        return ResponseEntity.ok(stats);
    }

 @GetMapping("/user/post/{postId}")
public ResponseEntity<Reaction> getUserReaction(@PathVariable("postId") Long postId, Principal principal) {
    if (principal == null)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    User currentUser = userService.getUserByUsername(principal.getName());
    if (currentUser == null || currentUser.getId() == null)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // ✅ thêm kiểm tra null

    Reaction reaction = reactionService.getUserReaction(postId, currentUser.getId());
    if (reaction != null)
        return ResponseEntity.ok(reaction);
    return ResponseEntity.notFound().build();
}

}
