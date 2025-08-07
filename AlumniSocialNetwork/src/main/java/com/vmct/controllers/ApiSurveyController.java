package com.vmct.controllers;

import com.vmct.dto.SurveyListDTO;
import com.vmct.dto.SurveyDetailDTO;
import com.vmct.pojo.User;
import com.vmct.services.SurveyService;
import com.vmct.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/secure/survey")
@CrossOrigin
public class ApiSurveyController {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<SurveyListDTO>> getAllSurveys(@RequestParam Map<String, String> params) {
        List<SurveyListDTO> surveyList = surveyService.getAllSurveysDTO(params);
        return ResponseEntity.ok(surveyList);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SurveyListDTO>> searchSurveys(@RequestParam("title") String title) {
        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<SurveyListDTO> surveyList = surveyService.getSurveysByTitleDTO(title);
        return ResponseEntity.ok(surveyList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyDetailDTO> getSurveyById(@PathVariable("id") Long id) {
        SurveyDetailDTO detailDTO = surveyService.getSurveyDetailById(id);
        if (detailDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detailDTO);
    }

    @GetMapping("/stats/{id}")
    public ResponseEntity<?> getSurveyStats(@PathVariable("id") Long id) {
        Map<String, Integer> stats = surveyService.getSurveyStatsById(id);
        if (stats == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<?> voteSurvey(@PathVariable("id") Long id, @RequestBody Map<String, Long> payload, Principal principal) {
        Long optionId = payload.get("optionId");
        if (optionId == null) {
            return ResponseEntity.badRequest().body("Missing optionId");
        }

        User currentUser = userService.getUserByUsername(principal.getName());
        boolean success = surveyService.addResponse(id, optionId, currentUser);

        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(500).body("Failed to vote");
        }
    }
}
