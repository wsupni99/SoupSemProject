package ru.itis.repositories.interfaces;

import ru.itis.entities.TaskLog;
import ru.itis.util.CrudRepository;

import java.util.List;

public interface TaskLogRepository extends CrudRepository<TaskLog, Long> {
    List<TaskLog> findByTaskId(Long taskId);

    List<TaskLog> findByUserId(Long userId);
}
