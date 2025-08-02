package com.vmct.services.impl;

import com.vmct.repositories.StatsRepository;
import com.vmct.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StatsServiceImpl implements StatsService {

    @Autowired
    private StatsRepository statsRepository;

    @Override
    public List<Object[]> countUsersByTime(String time, int year) {
        return statsRepository.countUsersByTime(time, year);
    }

    @Override
    public List<Object[]> countPostsByTime(String time, int year) {
        return statsRepository.countPostsByTime(time, year);
    }
}
