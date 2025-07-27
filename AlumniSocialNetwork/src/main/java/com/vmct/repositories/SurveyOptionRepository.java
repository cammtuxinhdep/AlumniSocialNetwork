package com.vmct.repositories;

import com.vmct.pojo.SurveyOption;

public interface SurveyOptionRepository {
    SurveyOption getSurveyOptionById(Long id);
    boolean addOrUpdateSurveyOption(SurveyOption option);
    boolean deleteSurveyOption(Long id);
}