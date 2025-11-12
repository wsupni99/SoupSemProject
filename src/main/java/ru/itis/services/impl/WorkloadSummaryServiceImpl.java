package ru.itis.services.impl;

import ru.itis.entities.WorkloadSummary;
import ru.itis.entities.Task;
import ru.itis.repositories.interfaces.TaskRepository;
import ru.itis.services.interfaces.WorkloadSummaryService;

import java.util.List;
import java.util.stream.Collectors;

public class WorkloadSummaryServiceImpl implements WorkloadSummaryService {
    private final TaskRepository taskRepository;

    public WorkloadSummaryServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public WorkloadSummary buildSummary(Long userId, Long projectId, Long sprintId) {
        List<Task> userTasks = taskRepository.findByUserId(userId)
                .stream()
                .filter(t ->
                        (projectId == null || projectId.equals(t.getProjectId())) &&
                                (sprintId == null || sprintId.equals(t.getSprintId()))
                )
                .collect(Collectors.toList());

        int openCount = (int) userTasks.stream()
                .filter(t -> !"Done".equals(t.getStatus()))
                .count();
        int closedCount = (int) userTasks.stream()
                .filter(t -> "Done".equals(t.getStatus()))
                .count();

        WorkloadSummary summary = new WorkloadSummary();
        summary.setSummaryId(null); // Генерируется БД
        summary.setUserId(userId);
        summary.setProjectId(projectId);
        summary.setSprintId(sprintId);
        summary.setOpenTasksCount(openCount);
        summary.setClosedTasksCount(closedCount);

        return summary;
    }
}
