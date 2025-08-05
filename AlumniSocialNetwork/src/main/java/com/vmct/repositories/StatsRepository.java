package com.vmct.repositories;

import java.util.List;

public interface StatsRepository {
    List<Object[]> countUsersByTime(String type, int year);
    List<Object[]> countPostsByTime(String type, int year);
}
