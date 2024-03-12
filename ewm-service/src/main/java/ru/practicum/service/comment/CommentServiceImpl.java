package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.*;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;



    @Override
    @Transactional
    public Comment create(Long userId, Long eventId, Comment comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserValidationException("Такого пользователя не существует"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventValidationException("Такого события не найдено"));
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now());
        if (comment.getText() == null || comment.getText().isBlank()) {
            throw new CommentValidationException("Отсутствует текст комментария");
        }
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(long userId, Comment comment) {
        Comment old = commentRepository.findById(comment.getId())
                .orElseThrow(() -> new CategoryNotFoundException("Комментарий не найден"));
        if (!old.getAuthor().getId().equals(userId)) {
            throw new UserValidationException("редактировать комментарий может только автор");
        }
        if (comment.getText().isBlank()) {
            throw new CommentValidationException("Отсутствует текст комментария");
        }
        comment.setAuthor(old.getAuthor());
        comment.setEvent(old.getEvent());
        comment.setCreated(old.getCreated());
        return commentRepository.save(comment);
    }


    @Override
    public void delete(long commentId) {
        commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentValidationException("Комментарий не найден"));
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<Comment> getComments(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventValidationException("Событие не найдено"));
        return commentRepository.findAllByEvent(event);
    }
}
