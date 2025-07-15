/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vmct.repositories;

import java.util.List;

/**
 *
 * @author HP
 */
public interface SystemStatisticsRepository {
    List<SystemStatistics> getStatisticsByTypeAndPeriod(String type, String period);
}
