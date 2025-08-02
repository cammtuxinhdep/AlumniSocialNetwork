package com.vmct.services;

import java.util.List;

public interface StatsService {
    List<Object[]> countUsersByTime(String timeType, int year);     
    List<Object[]> countPostsByTime(String timeType, int year);     
}
