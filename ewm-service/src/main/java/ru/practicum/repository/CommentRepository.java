package ru.practicum.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;

@Repository
public interface CommentRepository {

    int countByEvent(Event event);
}
