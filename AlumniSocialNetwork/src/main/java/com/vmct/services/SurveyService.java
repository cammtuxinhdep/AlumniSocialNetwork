package com.vmct.services;

import com.vmct.dto.SurveyDetailDTO;
import com.vmct.dto.SurveyListDTO;
import com.vmct.pojo.Survey;
import com.vmct.pojo.User;
import java.util.List;
import java.util.Map;

public interface SurveyService {
    List<Survey> getAllSurveys(Map<String, String> params);
    Survey getSurveyById(Long id);
    boolean addOrUpdateSurvey(Survey survey);
    boolean deleteSurvey(Long id);
    List<Survey> getSurveysByTitle(String title);
    boolean addOption(Long surveyId, String optionText);
    boolean deleteOption(Long surveyId, Long optionId);
    Map<String, Integer> calculateResponseStats(Survey survey);
    int calculateResponseCount(Survey survey);
     boolean addResponse(Long surveyId, Long optionId,User user);
     List<SurveyListDTO> getAllSurveysDTO(Map<String, String> params);
List<SurveyListDTO> getSurveysByTitleDTO(String title);
SurveyDetailDTO getSurveyDetailById(Long id);
Map<String, Integer> getSurveyStatsById(Long id);

}