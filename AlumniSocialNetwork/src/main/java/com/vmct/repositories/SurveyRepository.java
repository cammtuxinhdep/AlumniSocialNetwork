package com.vmct.repositories;

import com.vmct.pojo.Survey;
import java.util.List;
import java.util.Map;

public interface SurveyRepository {
    Survey getSurveyById(Long id);
    boolean addOrUpdateSurvey(Survey survey);
    boolean deleteSurvey(Long id);
    List<Survey> getSurveysByTitle(String title);
    List<Survey> getAllSurveys(Map<String, String> params);
}