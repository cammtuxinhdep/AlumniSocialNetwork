//package com.vmct.controllers;
//
//import com.vmct.pojo.Survey;
//import com.vmct.pojo.SurveyOption;
//import com.vmct.pojo.User;
//import com.vmct.services.SurveyService;
//import com.vmct.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/secure/survey")
//@CrossOrigin
//public class ApiSurveyController {
//
//    @Autowired
//    private SurveyService surveyService;
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping
//    public ResponseEntity<List<Survey>> getAllSurveys(@RequestParam Map<String, String> params) {
//        return ResponseEntity.ok(surveyService.getAllSurveys(params));
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<List<Survey>> searchSurveys(@RequestParam("title") String title) {
//        if (title == null || title.trim().isEmpty())
//            return ResponseEntity.badRequest().build();
//
//        return ResponseEntity.ok(surveyService.getSurveysByTitle(title));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Survey> getSurveyById(@PathVariable Long id) {
//        Survey survey = surveyService.getSurveyById(id);
//        return (survey != null) ? ResponseEntity.ok(survey) : ResponseEntity.notFound().build();
//    }
//
//    @PostMapping
//    public ResponseEntity<?> createOrUpdateSurvey(@RequestBody Survey survey, Principal principal) {
//        if (survey == null)
//            return ResponseEntity.badRequest().build();
//
//        User currentUser = userService.getUserByUsername(principal.getName());
//        survey.setUserId(currentUser); // gán user cho khảo sát
//
//        boolean success = surveyService.addOrUpdateSurvey(survey);
//        return success ? ResponseEntity.status(201).body(survey) : ResponseEntity.internalServerError().build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteSurvey(@PathVariable Long id, Principal principal) {
//        Survey survey = surveyService.getSurveyById(id);
//        if (survey == null)
//            return ResponseEntity.notFound().build();
//
//        User currentUser = userService.getUserByUsername(principal.getName());
//        if (!survey.getUserId().getId().equals(currentUser.getId())) {
//            return ResponseEntity.status(403).build();
//        }
//
//        boolean success = surveyService.deleteSurvey(id);
//        return success ? ResponseEntity.noContent().build() : ResponseEntity.internalServerError().build();
//    }
//
//    @GetMapping("/{id}/options")
//    public ResponseEntity<?> getSurveyOptions(@PathVariable Long id) {
//        Survey survey = surveyService.getSurveyById(id);
//        if (survey == null)
//            return ResponseEntity.notFound().build();
//
//        return ResponseEntity.ok(survey.getSurveyOptionSet().stream().toList());
//    }
//
//    @PostMapping("/{id}/options")
//    public ResponseEntity<?> addSurveyOption(
//            @PathVariable Long id,
//            @RequestBody Map<String, String> payload,
//            Principal principal) {
//
//        Survey survey = surveyService.getSurveyById(id);
//        if (survey == null)
//            return ResponseEntity.notFound().build();
//
//        User currentUser = userService.getUserByUsername(principal.getName());
//        if (!survey.getUserId().getId().equals(currentUser.getId())) {
//            return ResponseEntity.status(403).build();
//        }
//
//        String optionText = payload.get("optionText");
//        if (optionText == null || optionText.trim().isEmpty())
//            return ResponseEntity.badRequest().body("Missing optionText");
//
//        boolean success = surveyService.addOption(id, optionText);
//        return success ? ResponseEntity.status(201).build() : ResponseEntity.internalServerError().build();
//    }
//
//    @DeleteMapping("/{surveyId}/options/{optionId}")
//    public ResponseEntity<?> deleteOption(
//            @PathVariable Long surveyId,
//            @PathVariable Long optionId,
//            Principal principal) {
//
//        Survey survey = surveyService.getSurveyById(surveyId);
//        if (survey == null)
//            return ResponseEntity.notFound().build();
//
//        User currentUser = userService.getUserByUsername(principal.getName());
//        if (!survey.getUserId().getId().equals(currentUser.getId())) {
//            return ResponseEntity.status(403).build();
//        }
//
//        boolean success = surveyService.deleteOption(surveyId, optionId);
//        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
//    }
//
//    @GetMapping("/stats/{id}")
//    public ResponseEntity<?> getSurveyStats(@PathVariable Long id) {
//        Survey survey = surveyService.getSurveyById(id);
//        if (survey == null)
//            return ResponseEntity.notFound().build();
//
//        Map<String, Integer> stats = surveyService.calculateResponseStats(survey);
//        stats.put("total", surveyService.calculateResponseCount(survey));
//        return ResponseEntity.ok(stats);
//    }
//
//    @PostMapping("/{id}/vote")
//    public ResponseEntity<?> voteSurvey(
//            @PathVariable Long id,
//            @RequestBody Map<String, Long> payload,
//            Principal principal) {
//
//        Long optionId = payload.get("optionId");
//        if (optionId == null)
//            return ResponseEntity.badRequest().body("Missing optionId");
//
//        User currentUser = userService.getUserByUsername(principal.getName());
//        boolean success = surveyService.addResponse(id, optionId, currentUser);
//
//        return success ? ResponseEntity.ok().build() : ResponseEntity.internalServerError().build();
//    }
//}
