package ru.itis.repositories.interfaces;

import ru.itis.entities.Task;
import ru.itis.util.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByUserId(Long userId);
    List<Task> findByStatus(String status);
    List<Task> findByParentTaskId(Long parentTaskId);

    List<Task> findBySprintId(Long sprintId);
}
