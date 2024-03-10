package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    int countByEvent(Event event);

    List<Comment> findAllByEvent(Event event);
}
