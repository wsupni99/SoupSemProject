package ru.itis.services.impl;

import ru.itis.entities.Task;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.repositories.interfaces.TaskRepository;
import ru.itis.services.interfaces.TaskService;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void create(Task task) {
        Date now = new Date(System.currentTimeMillis());
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        taskRepository.save(task);
    }

    @Override
    public Task getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        return task;
    }

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public void update(Task task) {
        taskRepository.update(task);
    }

    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> getSubTasks(Long parentTaskId) {
        return taskRepository.findByParentTaskId(parentTaskId);
    }

    @Override
    public List<Task> getBySprintId(Long sprintId) {
        return taskRepository.findBySprintId(sprintId);
    }

    @Override
    public Object getByProjectId(Long id) {
        return taskRepository.findByProjectId(id);
    }

    @Override
    public List<Task> getFilteredTasks(Map<String, String[]> parameterMap) {
        String projectId = getParam(parameterMap, "project_id");
        String userId = getParam(parameterMap, "user_id");
        String status = getParam(parameterMap, "status");
        String sprintId = getParam(parameterMap, "sprint_id");

        return taskRepository.findWithFilters(
                projectId != null ? Long.parseLong(projectId) : null,
                userId != null ? Long.parseLong(userId) : null,
                status,
                sprintId != null ? Long.parseLong(sprintId) : null
        );
    }

    @Override
    public String getParam(Map<String, String[]> map, String key) {
        String[] arr = map.get(key);
        return arr != null && arr.length > 0 ? arr[0] : null;
    }
}