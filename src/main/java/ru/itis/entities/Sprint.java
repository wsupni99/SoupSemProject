package ru.itis.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sprint {
    private Long sprintId;
    private Long projectId;
    private String name;
    private Date startDate;
    private Date endDate;
}
