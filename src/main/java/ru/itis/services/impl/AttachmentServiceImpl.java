package ru.itis.services.impl;

import ru.itis.entities.Attachment;
import ru.itis.repositories.interfaces.AttachmentRepository;
import ru.itis.services.interfaces.AttachmentService;

import java.util.List;

public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public void create(Attachment attachment) {
        attachmentRepository.save(attachment);
    }

    @Override
    public Attachment getById(Long id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));
    }

    @Override
    public List<Attachment> getAll() {
        return attachmentRepository.findAll();
    }

    @Override
    public void update(Attachment updated) {
        Attachment existing = getById(updated.getAttachmentId());
        existing.setTaskId(updated.getTaskId());
        existing.setFileName(updated.getFileName());
        existing.setFileUrl(updated.getFileUrl());
        existing.setFileType(updated.getFileType());
        attachmentRepository.save(existing);
    }


    @Override
    public void delete(Long id) {
        attachmentRepository.deleteById(id);
    }
}
