package ru.itis.services.interfaces;

import ru.itis.entities.Task;

import java.util.List;
import java.util.Map;

public interface TaskService {
    void create(Task task);

    Task getById(Long id);

    List<Task> getAll();

    void update(Long id, Task updated);

    void delete(Long id);

    List<Task> getSubTasks(Long parentTaskId);

    List<Task> getBySprintId(Long sprintId);

    Object getByProjectId(Long id);

    List<Task> getFilteredTasks(Map<String, String[]> parameterMap);
}
