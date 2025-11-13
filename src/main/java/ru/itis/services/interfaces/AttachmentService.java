package ru.itis.services.interfaces;

import ru.itis.entities.Attachment;
import java.util.List;

public interface AttachmentService {
    void create(Attachment attachment);
    Attachment getById(Long id);
    List<Attachment> getAll();
    void update(Attachment updated);
    void delete(Long id);
}
