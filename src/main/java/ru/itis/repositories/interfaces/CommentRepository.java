package ru.itis.repositories.interfaces;

import ru.itis.entities.Comment;
import ru.itis.util.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByTaskId(Long taskId);
}
