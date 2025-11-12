package ru.itis.services.interfaces;

import ru.itis.entities.WorkloadSummary;

public interface WorkloadSummaryService {
    WorkloadSummary buildSummary(Long userId, Long projectId, Long sprintId);
}
