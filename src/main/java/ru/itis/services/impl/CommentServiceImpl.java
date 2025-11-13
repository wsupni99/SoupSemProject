package ru.itis.services.impl;

import ru.itis.entities.Comment;
import ru.itis.repositories.interfaces.CommentRepository;
import ru.itis.services.interfaces.CommentService;

import java.util.List;

public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void create(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public Comment getById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public void update(Comment updated) {
        Comment existing = getById(updated.getCommentId());
        existing.setTaskId(updated.getTaskId());
        existing.setUserId(updated.getUserId());
        existing.setText(updated.getText());
        existing.setCreatedAt(updated.getCreatedAt());
        commentRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
}
