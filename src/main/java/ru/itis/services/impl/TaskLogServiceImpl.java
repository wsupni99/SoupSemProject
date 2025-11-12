package ru.itis.services.impl;

import ru.itis.entities.TaskLog;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.repositories.interfaces.TaskLogRepository;
import ru.itis.services.interfaces.TaskLogService;

import java.util.List;

public class TaskLogServiceImpl implements TaskLogService {
    private final TaskLogRepository taskLogRepository;

    public TaskLogServiceImpl(TaskLogRepository taskLogRepository) {
        this.taskLogRepository = taskLogRepository;
    }

    @Override
    public void create(TaskLog taskLog) {
        taskLogRepository.save(taskLog);
    }

    @Override
    public TaskLog getById(Long id) {
        return taskLogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TaskLog not found"));
    }

    @Override
    public List<TaskLog> getAll() {
        return taskLogRepository.findAll();
    }

    @Override
    public void update(Long id, TaskLog updated) {
        TaskLog existing = getById(id);
        existing.setTaskId(updated.getTaskId());
        existing.setUserId(updated.getUserId());
        existing.setAction(updated.getAction());
        existing.setChangedAt(updated.getChangedAt());
        taskLogRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        taskLogRepository.deleteById(id);
    }
}

