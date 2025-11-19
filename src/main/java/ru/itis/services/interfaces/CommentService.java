package ru.itis.services.interfaces;

import ru.itis.entities.Comment;

import java.util.List;

public interface CommentService {
    void create(Comment comment);
    void update(Comment updated);
    void delete(Long id);

    Comment getById(Long id);
    List<Comment> getByTaskId(Long taskId);
    List<Comment> getAll();
}
