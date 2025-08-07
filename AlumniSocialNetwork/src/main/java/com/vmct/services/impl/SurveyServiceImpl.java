//package com.vmct.services.impl;
//
//import com.vmct.pojo.Survey;
//import com.vmct.pojo.SurveyOption;
//import com.vmct.pojo.SurveyResponse;
//import com.vmct.pojo.User;
//import com.vmct.repositories.SurveyRepository;
//import com.vmct.repositories.SurveyOptionRepository;
//import com.vmct.services.SurveyService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//@Service
//public class SurveyServiceImpl implements SurveyService {
//
//    @Autowired
//    private SurveyRepository surveyRepository;
//
//    @Autowired
//    private SurveyOptionRepository optionRepository;
//
//    @Override
//    public List<Survey> getAllSurveys(Map<String, String> params) {
//        return surveyRepository.getAllSurveys(params);
//    }
//
//    @Override
//    public Survey getSurveyById(Long id) {
//        return surveyRepository.getSurveyById(id);
//    }
//
//    @Override
//    public boolean addOrUpdateSurvey(Survey survey) {
//        if (survey.getId() == null) {
//            survey.setCreatedAt(new Date());
//        } else {
//            Survey oldSurvey = surveyRepository.getSurveyById(survey.getId());
//            if (oldSurvey != null) {
//                survey.setCreatedAt(oldSurvey.getCreatedAt());
//            }
//        }
//        return surveyRepository.addOrUpdateSurvey(survey);
//    }
//
//    @Override
//    public boolean deleteSurvey(Long id) {
//        return surveyRepository.deleteSurvey(id);
//    }
//
//    @Override
//    public List<Survey> getSurveysByTitle(String title) {
//        return surveyRepository.getSurveysByTitle(title);
//    }
//
//    @Override
//    public boolean addOption(Long surveyId, String optionText) {
//        Survey survey = surveyRepository.getSurveyById(surveyId);
//        if (survey == null) return false;
//
//        SurveyOption option = new SurveyOption();
//        option.setOptionText(optionText);
//        option.setSurveyId(survey);
//
//        // Thêm option vào danh sách
//        survey.getSurveyOptionSet().add(option);
//        return surveyRepository.addOrUpdateSurvey(survey);
//    }
//
//    @Override
//    public boolean deleteOption(Long surveyId, Long optionId) {
//        Survey survey = surveyRepository.getSurveyById(surveyId);
//        if (survey == null) return false;
//
//        Set<SurveyOption> options = survey.getSurveyOptionSet();
//        SurveyOption toRemove = null;
//
//        for (SurveyOption option : options) {
//            if (option.getId().equals(optionId)) {
//                toRemove = option;
//                break;
//            }
//        }
//
//        if (toRemove != null) {
//            options.remove(toRemove);
//            return surveyRepository.addOrUpdateSurvey(survey);
//        }
//
//        return false;
//    }
//
//    @Override
//    public Map<String, Integer> calculateResponseStats(Survey survey) {
//        Map<String, Integer> stats = new HashMap<>();
//
//        if (survey != null && survey.getSurveyResponseSet() != null) {
//            for (SurveyResponse response : survey.getSurveyResponseSet()) {
//                String optionText = response.getOptionId().getOptionText();
//
//                if (stats.containsKey(optionText)) {
//                    int current = stats.get(optionText);
//                    stats.put(optionText, current + 1);
//                } else {
//                    stats.put(optionText, 1);
//                }
//            }
//        }
//
//        return stats;
//    }
//
//    @Override
//    public int calculateResponseCount(Survey survey) {
//        if (survey != null && survey.getSurveyResponseSet() != null) {
//            return survey.getSurveyResponseSet().size();
//        }
//        return 0;
//    }
//
//    @Override
//    public boolean addResponse(Long surveyId, Long optionId, User user) {
//        try {
//            Survey survey = surveyRepository.getSurveyById(surveyId);
//            SurveyOption option = optionRepository.getSurveyOptionById(optionId);
//
//            if (survey == null || option == null) return false;
//
//            SurveyResponse response = new SurveyResponse();
//            response.setSurveyId(survey);
//            response.setOptionId(option);
//            response.setCreatedAt(new Date());
//
//            survey.getSurveyResponseSet().add(response);
//            return surveyRepository.addOrUpdateSurvey(survey);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//}
