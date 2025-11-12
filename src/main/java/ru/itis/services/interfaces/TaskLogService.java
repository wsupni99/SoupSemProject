package ru.itis.services.interfaces;

import ru.itis.entities.TaskLog;

import java.util.List;

public interface TaskLogService {
    void create(TaskLog taskLog);

    TaskLog getById(Long id);

    List<TaskLog> getAll();

    void update(Long id, TaskLog updated);

    void delete(Long id);
}
