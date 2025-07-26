<<<<<<< Updated upstream
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
=======
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

    // 1. Danh sách khảo sát
    @GetMapping
    public String listSurveys(@RequestParam Map<String, String> params, Model model) {
        List<Survey> surveys = surveyService.getAllSurveys(params);
        model.addAttribute("surveys", surveys);
        return "survey-list";
    }

    // 2. Form thêm khảo sát
    @GetMapping("/add")
    public String addSurveyForm(Model model) {
        model.addAttribute("survey", new Survey());
        return "survey-form";
    }

    // 3. Form sửa khảo sát
    @GetMapping("/edit/{id}")
    public String editSurveyForm(@PathVariable("id") Long id, Model model) {
        Survey survey = surveyService.getSurveyById(id);
        if (survey == null)
            return "redirect:/survey";

        model.addAttribute("survey", survey);
        return "survey-form";
    }

    // 4. Lưu khảo sát (add/update)
    @PostMapping("/save")
    public String saveSurvey(@ModelAttribute Survey survey) {
        surveyService.addOrUpdateSurvey(survey);
        return "redirect:/survey";
    }

    // 5. Xóa khảo sát
    @GetMapping("/delete/{id}")
    public String deleteSurvey(@PathVariable("id") Long id) {
        surveyService.deleteSurvey(id);
        return "redirect:/survey";
    }

    // 6. Form thêm lựa chọn
    @GetMapping("/{id}/options")
    public String showOptionForm(@PathVariable("id") Long id, Model model) {
        Survey survey = surveyService.getSurveyById(id);
        if (survey == null)
            return "redirect:/survey";

        model.addAttribute("survey", survey);
        model.addAttribute("newOption", new SurveyOption());
        return "survey-options";
    }

    // 7. Thêm lựa chọn
    @PostMapping("/{id}/options")
    public String addOption(@PathVariable("id") Long id, @ModelAttribute("newOption") SurveyOption option) {
        surveyService.addOption(id, option.getOptionText());
        return "redirect:/survey/" + id + "/options";
    }

    // 8. Xóa lựa chọn
    @GetMapping("/{surveyId}/options/delete/{optionId}")
    public String deleteOption(
            @PathVariable("surveyId") Long surveyId,
            @PathVariable("optionId") Long optionId
    ) {
        surveyService.deleteOption(surveyId, optionId);
        return "redirect:/survey/" + surveyId + "/options";
    }

    // 9. Thống kê phản hồi
    @GetMapping("/stats/{id}")
    public String viewStats(@PathVariable("id") Long id, Model model) {
        Survey survey = surveyService.getSurveyById(id);
        if (survey == null)
            return "redirect:/survey";

        model.addAttribute("survey", survey);
        model.addAttribute("stats", surveyService.calculateResponseStats(survey));
        model.addAttribute("total", surveyService.calculateResponseCount(survey));
        return "survey-stats";
>>>>>>> Stashed changes
    }
}
