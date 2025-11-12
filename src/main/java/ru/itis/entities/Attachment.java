package ru.itis.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Attachment {
    private Long attachmentId;
    private Long taskId;
    private String fileName;
    private String fileUrl;
    private String fileType;


}