package com.qoobico.remindme.dto;

import java.util.List;

public class ArrayStatsDTO {

    private List<StatisticsDTO> stats;

    public void ArrayStatsDTO(List<StatisticsDTO> stats){
        this.stats = stats;
    }

    public List<StatisticsDTO> getStats() {
        return stats;
    }

    public void setStats(List<StatisticsDTO> stats) {
        this.stats = stats;
    }
}
