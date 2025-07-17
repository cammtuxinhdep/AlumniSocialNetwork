/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vmct.controllers;

/**
 *
 * @author Thanh Nhat
 */
public class SurveyController {
    @Autowired
    private SurveyService surveyService;

    @GetMapping("/")
    public String listSurveys(Model model) {
        model.addAttribute("surveys", surveyService.getAllSurveys());
        return "surveys/list";
    }

    @GetMapping("/add")
    public String addSurveyForm(Model model) {
        model.addAttribute("survey", new Surveys());
        return "surveys/add";
    }

    @PostMapping("/add")
    public String addSurvey(@ModelAttribute("survey") @Valid Surveys survey, BindingResult rs) {
        if (!rs.hasErrors()) {
            surveyService.addSurvey(survey);
            return "redirect:/surveys/";
        }
        return "surveys/add";
    }

    @GetMapping("/delete/{id}")
    public String deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurvey(id);
        return "redirect:/surveys/";
    }
}
