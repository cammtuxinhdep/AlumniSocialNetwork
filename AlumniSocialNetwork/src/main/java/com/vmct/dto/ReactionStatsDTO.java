package com.vmct.dto;

import java.util.Map;

public class ReactionStatsDTO {
    private Map<String, Integer> stats;

    public ReactionStatsDTO(Map<String, Integer> stats) {
        this.stats = stats;
    }

    public Map<String, Integer> getStats() {
        return stats;
    }

    public void setStats(Map<String, Integer> stats) {
        this.stats = stats;
    }
}
