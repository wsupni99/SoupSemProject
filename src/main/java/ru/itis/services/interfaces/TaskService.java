package ru.itis.services.interfaces;

import ru.itis.entities.Task;

import java.util.List;
import java.util.Map;

public interface TaskService {
    void create(Task task);
    void update(Task updated);
    void delete(Long id);

    List<Task> getByProjectId(Long id);
    List<Task> getFilteredTasks(Map<String, String[]> parameterMap);
    String getParam(Map<String, String[]> map, String key);
    Task getById(Long id);
    List<Task> getAll();
}
