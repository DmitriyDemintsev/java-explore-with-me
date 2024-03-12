package ru.practicum.service.comment;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Comment;

import java.util.List;

@Transactional(readOnly = true)
public interface CommentService {
    @Transactional
    Comment create(Long userId, Long itemId, Comment comment);

    @Transactional
    Comment update(long userId, Comment comment);

    @Transactional
    void delete(long commentId);

    List<Comment> getComments(Long eventId);
}
