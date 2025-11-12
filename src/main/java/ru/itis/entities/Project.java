package ru.itis.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    private Long projectId;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private String status;
    private Long managerId;
}