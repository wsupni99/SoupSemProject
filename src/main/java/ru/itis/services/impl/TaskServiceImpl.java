package ru.itis.services.impl;

import ru.itis.entities.Task;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.repositories.interfaces.TaskRepository;
import ru.itis.services.interfaces.TaskService;

import java.util.List;
import java.util.Map;

public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void create(Task task) {
        taskRepository.save(task);
    }

    @Override
    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
    }

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public void update(Long id, Task updated) {
        Task existing = getById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPriority(updated.getPriority());
        existing.setStatus(updated.getStatus());
        existing.setCreatedAt(updated.getCreatedAt());
        existing.setUpdatedAt(updated.getUpdatedAt());
        existing.setDeadline(updated.getDeadline());
        existing.setParentTaskId(updated.getParentTaskId());
        existing.setProjectId(updated.getProjectId());
        existing.setSprintId(updated.getSprintId());
        existing.setUserId(updated.getUserId());
        taskRepository.save(existing);
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

        if (projectId != null) {
            return taskRepository.findByProjectId(Long.parseLong(projectId));
        }
        if (userId != null) {
            return taskRepository.findByUserId(Long.parseLong(userId));
        }
        if (sprintId != null) {
            return taskRepository.findBySprintId(Long.parseLong(sprintId));
        }
        if (status != null) {
            return taskRepository.findByStatus(status);
        }
        return taskRepository.findAll();
    }

    private String getParam(Map<String, String[]> map, String key) {
        String[] arr = map.get(key);
        return arr != null && arr.length > 0 ? arr[0] : null;
    }
}