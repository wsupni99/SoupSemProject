package ru.itis.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private Long commentId;
    private Long taskId;
    private Long userId;
    private String text;
    private Date createdAt;
}