package ru.itis.dto.project;

import lombok.Data;

import java.sql.Date;

@Data
public class ProjectRequestDto {
    private Long projectId;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private String status;
    private Long managerId;
}
