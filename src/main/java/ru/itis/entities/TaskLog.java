package ru.itis.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskLog {
    private Long logId;
    private Long taskId;
    private Long userId;
    private String action;
    private Date changedAt;
}