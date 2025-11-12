package ru.itis.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Long roleId;
    private String roleName;
    private String description;

    public static final String ADMIN_ROLE_NAME = "admin";
}