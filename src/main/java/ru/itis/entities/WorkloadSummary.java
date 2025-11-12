package ru.itis.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkloadSummary {
    private Long summaryId;
    private Long userId;
    private Long projectId;
    private Integer openTasksCount;
    private Integer closedTasksCount;
    private Long sprintId;
}