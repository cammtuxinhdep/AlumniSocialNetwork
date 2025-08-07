package com.vmct.services.impl;

import com.vmct.dto.SurveyDetailDTO;
import com.vmct.dto.SurveyListDTO;
import com.vmct.dto.SurveyOptionDTO;
import com.vmct.dto.SurveyResponseDTO;
import com.vmct.pojo.Survey;
import com.vmct.pojo.SurveyOption;
import com.vmct.pojo.SurveyResponse;
import com.vmct.pojo.User;
import com.vmct.repositories.SurveyOptionRepository;
import com.vmct.repositories.SurveyRepository;
import com.vmct.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private SurveyOptionRepository optionRepository;

    private static final int PAGE_SIZE = 10;

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
            survey.setCreatedAt(new Date());
        } else {
            Survey oldSurvey = surveyRepository.getSurveyById(survey.getId());
            if (oldSurvey != null) {
                survey.setCreatedAt(oldSurvey.getCreatedAt());
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
        if (survey == null) {
            return false;
        }

        SurveyOption option = new SurveyOption();
        option.setOptionText(optionText);
        option.setSurveyId(survey);

        survey.getSurveyOptionSet().add(option);
        return surveyRepository.addOrUpdateSurvey(survey);
    }

    @Override
    public boolean deleteOption(Long surveyId, Long optionId) {
        Survey survey = surveyRepository.getSurveyById(surveyId);
        if (survey == null) {
            return false;
        }

        Set<SurveyOption> options = survey.getSurveyOptionSet();
        SurveyOption toRemove = null;

        for (SurveyOption option : options) {
            if (option.getId().equals(optionId)) {
                toRemove = option;
                break;
            }
        }

        if (toRemove != null) {
            options.remove(toRemove);
            return surveyRepository.addOrUpdateSurvey(survey);
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
    public boolean addResponse(Long surveyId, Long optionId, User user) {
        try {
            Survey survey = surveyRepository.getSurveyById(surveyId);
            SurveyOption option = optionRepository.getSurveyOptionById(optionId);

            if (survey == null || option == null) {
                return false;
            }

            SurveyResponse response = new SurveyResponse();
            response.setSurveyId(survey);
            response.setOptionId(option);
            response.setUserId(user);
            response.setCreatedAt(new Date());

            survey.getSurveyResponseSet().add(response);
            return surveyRepository.addOrUpdateSurvey(survey);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public SurveyOptionDTO toSurveyOptionDTO(SurveyOption option) {
        int voteCount = option.getSurveyResponseSet() != null ? option.getSurveyResponseSet().size() : 0;
        SurveyOptionDTO dto = new SurveyOptionDTO();
        dto.setId(option.getId());
        dto.setOptionText(option.getOptionText());
        dto.setVoteCount(voteCount);
        return dto;
    }

    public SurveyResponseDTO toSurveyResponseDTO(SurveyResponse response) {
        SurveyResponseDTO dto = new SurveyResponseDTO();
        dto.setId(response.getId());
        dto.setUserId(response.getUserId() != null ? response.getUserId().getId() : null);
        dto.setUserName(response.getUserId() != null ? response.getUserId().getUsername() : null);
        dto.setOptionId(response.getOptionId() != null ? response.getOptionId().getId() : null);
        dto.setOptionText(response.getOptionId() != null ? response.getOptionId().getOptionText() : null);
        dto.setCreatedAt(response.getCreatedAt());
        return dto;
    }

    public SurveyDetailDTO toSurveyDetailDTO(Survey survey) {
        SurveyDetailDTO dto = new SurveyDetailDTO();
        dto.setId(survey.getId());
        dto.setTitle(survey.getTitle());
        dto.setDescription(survey.getDescription());
        dto.setCreatedAt(survey.getCreatedAt());

        dto.setOptions(
                survey.getSurveyOptionSet() == null
                ? Collections.emptyList()
                : survey.getSurveyOptionSet().stream()
                        .map(this::toSurveyOptionDTO)
                        .collect(Collectors.toList())
        );

        dto.setResponses(
                survey.getSurveyResponseSet() == null
                ? Collections.emptyList()
                : survey.getSurveyResponseSet().stream()
                        .map(this::toSurveyResponseDTO)
                        .collect(Collectors.toList())
        );

        return dto;
    }

    public SurveyListDTO toSurveyListDTO(Survey survey) {
        SurveyListDTO dto = new SurveyListDTO();
        dto.setId(survey.getId());
        dto.setTitle(survey.getTitle());
        dto.setCreatedAt(survey.getCreatedAt());
        dto.setDescription(survey.getDescription());
        dto.setOptionCount(survey.getSurveyOptionSet() != null ? survey.getSurveyOptionSet().size() : 0);
        dto.setResponseCount(survey.getSurveyResponseSet() != null ? survey.getSurveyResponseSet().size() : 0);
        return dto;
    }

    @Override
    public List<SurveyListDTO> getAllSurveysDTO(Map<String, String> params) {
        List<Survey> surveys = getAllSurveys(params);
        return surveys.stream()
                .map(this::toSurveyListDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SurveyListDTO> getSurveysByTitleDTO(String title) {
        List<Survey> surveys = getSurveysByTitle(title);
        return surveys.stream()
                .map(this::toSurveyListDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SurveyDetailDTO getSurveyDetailById(Long id) {
        Survey survey = getSurveyById(id);
        if (survey == null) {
            return null;
        }
        return toSurveyDetailDTO(survey);
    }

    @Override
    public Map<String, Integer> getSurveyStatsById(Long id) {
        Survey survey = getSurveyById(id);
        if (survey == null) {
            return Collections.emptyMap();
        }

        Map<String, Integer> stats = calculateResponseStats(survey);
        stats.put("total", calculateResponseCount(survey));
        return stats;
    }
}
