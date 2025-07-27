package com.vmct.services.impl;

import com.vmct.pojo.Survey;
import com.vmct.pojo.SurveyOption;
import com.vmct.pojo.SurveyResponse;
import com.vmct.repositories.SurveyRepository;
import com.vmct.repositories.SurveyOptionRepository;
import com.vmct.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private SurveyOptionRepository optionRepository;

    @Override
    public List<Survey> getAllSurveys(Map<String, String> params) {
        return surveyRepository.getAllSurveys(params);
    }

    @Override
    public Survey getSurveyById(Long id) {
        return surveyRepository.getSurveyById(id);
    }

    @Override
public boolean addOrUpdateSurvey(Survey survey) {
    if (survey.getId() == null) {
        // Trường hợp thêm mới
        survey.setCreatedAt(new Date());
    } else {
        // Trường hợp cập nhật, cần giữ lại createdAt gốc
        Survey old = surveyRepository.getSurveyById(survey.getId());
        if (old != null) {
            survey.setCreatedAt(old.getCreatedAt());
        }
    }
    return surveyRepository.addOrUpdateSurvey(survey);
}

    @Override
    public boolean deleteSurvey(Long id) {
        return surveyRepository.deleteSurvey(id);
    }

    @Override
    public List<Survey> getSurveysByTitle(String title) {
        return surveyRepository.getSurveysByTitle(title);
    }

    @Override
    public boolean addOption(Long surveyId, String optionText) {
        Survey survey = surveyRepository.getSurveyById(surveyId);
        if (survey != null) {
            SurveyOption option = new SurveyOption();
            option.setOptionText(optionText);
            option.setSurveyId(survey);
            survey.getSurveyOptionSet().add(option);
            return surveyRepository.addOrUpdateSurvey(survey);
        }
        return false;
    }

    @Override
    public boolean deleteOption(Long surveyId, Long optionId) {
        Survey survey = surveyRepository.getSurveyById(surveyId);
        if (survey != null) {
            boolean removed = survey.getSurveyOptionSet().removeIf(option -> option.getId().equals(optionId));
            if (removed) {
                return surveyRepository.addOrUpdateSurvey(survey);
            }
        }
        return false;
    }

    @Override
    public Map<String, Integer> calculateResponseStats(Survey survey) {
        Map<String, Integer> stats = new HashMap<>();
        if (survey != null && survey.getSurveyResponseSet() != null) {
            for (SurveyResponse response : survey.getSurveyResponseSet()) {
                String optionText = response.getOptionId().getOptionText();
                stats.put(optionText, stats.getOrDefault(optionText, 0) + 1);
            }
        }
        return stats;
    }

    @Override
    public int calculateResponseCount(Survey survey) {
        if (survey != null && survey.getSurveyResponseSet() != null) {
            return survey.getSurveyResponseSet().size();
        }
        return 0;
    }

    @Override
    public boolean addResponse(Long surveyId, Long optionId) {
        try {
            Survey survey = surveyRepository.getSurveyById(surveyId);
            SurveyOption option = optionRepository.getSurveyOptionById(optionId);

            if (survey != null && option != null) {
                SurveyResponse response = new SurveyResponse();
                response.setSurveyId(survey);
                response.setOptionId(option);
                response.setCreatedAt(new Date());

                survey.getSurveyResponseSet().add(response);

                return surveyRepository.addOrUpdateSurvey(survey);
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi để dễ debug
            return false;
        }
    }
}
