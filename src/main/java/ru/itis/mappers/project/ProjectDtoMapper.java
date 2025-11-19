package ru.itis.mappers.project;

import ru.itis.dto.project.ProjectRequestDto;
import ru.itis.dto.project.ProjectResponseDto;
import ru.itis.entities.Project;
import ru.itis.entities.User;

public final class ProjectDtoMapper {

    private ProjectDtoMapper() {
    }

    public static Project toEntity(ProjectRequestDto dto) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setStatus(dto.getStatus());
        project.setManagerId(dto.getManagerId());
        return project;
    }

    public static ProjectResponseDto toResponseDto(Project project, User manager) {
        ProjectResponseDto dto = new ProjectResponseDto();
        dto.setProjectId(project.getProjectId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setStatus(project.getStatus());
        dto.setManagerName(manager == null ? null : manager.getName());
        return dto;
    }
}
