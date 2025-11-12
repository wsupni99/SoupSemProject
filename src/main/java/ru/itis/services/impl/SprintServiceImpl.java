package ru.itis.services.impl;

import ru.itis.entities.Sprint;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.repositories.interfaces.SprintRepository;
import ru.itis.services.interfaces.SprintService;

import java.util.List;

public class SprintServiceImpl implements SprintService {
    private final SprintRepository sprintRepository;

    public SprintServiceImpl(SprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
    }

    @Override
    public void create(Sprint sprint) {
        sprintRepository.save(sprint);
    }

    @Override
    public Sprint getById(Long id) {
        return sprintRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sprint not found"));
    }

    @Override
    public List<Sprint> getAll() {
        return sprintRepository.findAll();
    }

    @Override
    public void update(Long id, Sprint updated) {
        Sprint existing = getById(id);
        existing.setProjectId(updated.getProjectId());
        existing.setName(updated.getName());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        sprintRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        sprintRepository.deleteById(id);
    }
}
