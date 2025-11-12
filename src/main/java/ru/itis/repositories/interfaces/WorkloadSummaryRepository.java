package ru.itis.repositories.interfaces;

import ru.itis.entities.WorkloadSummary;
import ru.itis.util.CrudRepository;

import java.util.List;

public interface WorkloadSummaryRepository extends CrudRepository<WorkloadSummary, Long> {
    List<WorkloadSummary> findByUserId(Long userId);
    List<WorkloadSummary> findByProjectId(Long projectId);
    List<WorkloadSummary> findBySprintId(Long sprintId);
}