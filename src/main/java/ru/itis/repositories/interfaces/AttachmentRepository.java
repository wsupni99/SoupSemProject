package ru.itis.repositories.interfaces;

import ru.itis.entities.Attachment;
import ru.itis.util.CrudRepository;

import java.util.List;

public interface AttachmentRepository extends CrudRepository<Attachment, Long> {
    List<Attachment> findByTaskId(Long taskId);
}
