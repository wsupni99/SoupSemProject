package ru.itis.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private Long taskId;
    private String name;
    private String description;
    private String priority;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private Date deadline;
    private Long parentTaskId;
    private Long projectId;
    private Long sprintId;
    private Long userId;
}