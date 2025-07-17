/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Thanh Nhat
 */

@RestController
@RequestMapping("/api/surveys")
@CrossOrigin
public class ApiSurveyController {

    @Autowired
    private SurveyService surveyService;

    @GetMapping("/")
    public ResponseEntity<List<Surveys>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    @PostMapping("/add")
    public ResponseEntity<Surveys> createSurvey(@RequestBody Surveys survey) {
        return ResponseEntity.ok(surveyService.addSurvey(survey));
    }

    @DeleteMapping("/{id}")
    public void deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurvey(id);
    }
}
