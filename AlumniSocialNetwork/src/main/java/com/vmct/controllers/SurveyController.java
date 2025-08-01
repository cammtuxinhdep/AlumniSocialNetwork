package com.vmct.controllers;

import com.vmct.pojo.Survey;
import com.vmct.pojo.SurveyOption;
import com.vmct.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/survey")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @GetMapping
    public String listSurveys(@RequestParam Map<String, String> params, Model model) {
        List<Survey> surveys = surveyService.getAllSurveys(params);
        model.addAttribute("surveys", surveys);
        return "survey-list";
    }

    @GetMapping("/add")
    public String addSurveyForm(Model model) {
        model.addAttribute("survey", new Survey());
        return "survey-form";
    }

    @GetMapping("/edit/{id}")
    public String editSurveyForm(@PathVariable("id") Long id, Model model) {
        Survey survey = surveyService.getSurveyById(id);
        if (survey == null) {
            return "redirect:/survey";
        }

        model.addAttribute("survey", survey);
        return "survey-form";
    }

    @PostMapping("/save")
    public String saveSurvey(@ModelAttribute Survey survey) {
        surveyService.addOrUpdateSurvey(survey);
        return "redirect:/survey";
    }

    @GetMapping("/delete/{id}")
    public String deleteSurvey(@PathVariable("id") Long id) {
        surveyService.deleteSurvey(id);
        return "redirect:/survey";
    }

    @GetMapping("/{id}/options")
    public String showOptionForm(@PathVariable("id") Long id, Model model) {
        Survey survey = surveyService.getSurveyById(id);
        if (survey == null) {
            return "redirect:/survey";
        }

        model.addAttribute("survey", survey);
        model.addAttribute("newOption", new SurveyOption());
        return "survey-options";
    }

    @PostMapping("/{id}/options")
    public String addOption(@PathVariable("id") Long id, @ModelAttribute("newOption") SurveyOption option) {
        surveyService.addOption(id, option.getOptionText());
        return "redirect:/survey/" + id + "/options";
    }

    @GetMapping("/{surveyId}/options/delete/{optionId}")
    public String deleteOption(
            @PathVariable("surveyId") Long surveyId,
            @PathVariable("optionId") Long optionId
    ) {
        surveyService.deleteOption(surveyId, optionId);
        return "redirect:/survey/" + surveyId + "/options";
    }

    @GetMapping("/stats/{id}")
    public String viewStats(@PathVariable("id") Long id, Model model) {
        Survey survey = surveyService.getSurveyById(id);
        if (survey == null) {
            return "redirect:/survey";
        }

        model.addAttribute("survey", survey);
        model.addAttribute("stats", surveyService.calculateResponseStats(survey));
        model.addAttribute("total", surveyService.calculateResponseCount(survey));
        return "survey-stats";
    }
}
